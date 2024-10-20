package com.example.WeatherAPP.service.impl;

import com.example.WeatherAPP.model.AlertThresholds;
import com.example.WeatherAPP.model.ResponseModel;
import com.example.WeatherAPP.model.WeatherModel;
import com.example.WeatherAPP.repository.ResponseRepository;
import com.example.WeatherAPP.service.WeatherService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class WeatherServiceImpl implements WeatherService {

    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather";
    private static final String API_KEY = "6f12ae61b995ae19aa173a138291577c";

    private static final List<String> cities = new ArrayList<>(Arrays.asList("Delhi", "Bangalore", "Kolkata", "Chennai", "Hyderabad", "Mumbai"));

    private static final List<ResponseModel> responses = new ArrayList<>();

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    @Scheduled(fixedRate = 300000)
    public List<ResponseModel> getCurrentWeather() {
        cities.forEach(
                city -> {
                    String url = UriComponentsBuilder.fromHttpUrl(API_URL)
                            .queryParam("q", city)
                            .queryParam("appid", API_KEY)
                            .build()
                            .toUriString();

                    WeatherModel response = restTemplate.getForObject(url, WeatherModel.class);

                    ResponseModel responseModel = new ResponseModel();

                    responseModel.setCity(city);
                    responseModel.setTimestamp(LocalDateTime.now());
                    responseModel.setDominantCondition(response.getWeather().get(0).getDescription());
                    float maxTemp = response.getMain().getTemp_max();
                    float minTemp = response.getMain().getTemp_min();

                    float avgTemp = getAvgTemp(maxTemp, minTemp);

                    responseModel.setMaxTemp(formatTemperature(maxTemp));
                    responseModel.setMinTemp(formatTemperature(minTemp));
                    responseModel.setAvgTemp(formatTemperature(avgTemp));
                    responses.add(responseModel);
                }
        );

        persistWeatherData(responses);
        return responses;
    }

    @Transactional
    private void persistWeatherData(List<ResponseModel> responseModels) {
        responseModels.forEach(
                responseModel -> {
                    responseRepository.save(responseModel);
                }
        );
    }

    private String formatTemperature(float kelvinTemp) {
        float celsiusTemp = kelvinTemp - 273.15F;
        return String.format("%.2f", celsiusTemp);
    }

    private static float getAvgTemp(float maxTemp, float minTemp) {
        return (maxTemp + minTemp) / 2;
    }
}
