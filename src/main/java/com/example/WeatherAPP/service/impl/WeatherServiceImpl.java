package com.example.WeatherAPP.service.impl;

import com.example.WeatherAPP.config.AlertConfig;
import com.example.WeatherAPP.constant.Constants;
import com.example.WeatherAPP.model.AlertThresholds;
import com.example.WeatherAPP.model.ResponseModel;
import com.example.WeatherAPP.model.WeatherModel;
import com.example.WeatherAPP.repository.ConfigRepository;
import com.example.WeatherAPP.repository.ResponseRepository;
import com.example.WeatherAPP.service.WeatherService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Value("${spring.mail.username}")
    private String fromEmailId;

    @Autowired
    private JavaMailSender javaMailSender;

    private static final List<String> cities = new ArrayList<>(Arrays.asList("Delhi", "Bangalore", "Kolkata", "Chennai", "Hyderabad", "Mumbai"));

    private static final List<ResponseModel> responses = new ArrayList<>();

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final AlertThresholds thresholds = new AlertThresholds();
    private static int breachCount = 2;
    private LocalDateTime lastAlertTime;

    public WeatherServiceImpl() {
        thresholds.setConsecutiveBreaches(breachCount);
    }

    private Constants constants;

    private String alertMessage = "";

    @Override
    @Scheduled(fixedRate = 300000)
    public List<ResponseModel> getCurrentWeather() {
        cities.forEach(
                city -> {
                    String url = UriComponentsBuilder.fromHttpUrl(Constants.API_URL)
                            .queryParam("q", city)
                            .queryParam("appid", Constants.API_KEY)
                            .build()
                            .toUriString();

                    WeatherModel response = this.restTemplate.getForObject(url, WeatherModel.class);

                    ResponseModel responseModel = new ResponseModel();

                    handleWeatherAlerts(response);

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

    public void handleWeatherAlerts(WeatherModel response) {

        Optional<AlertConfig> alertConfigOptional = getAlertConfigFromDb();

        if(!alertConfigOptional.isPresent()) {
            return;
        }
        AlertConfig alertConfig = alertConfigOptional.get();
        int threshold = alertConfig.getThreshold();
        thresholds.setTemperatureThreshold(threshold);
        String city = alertConfig.getCity();
        if (response != null && response.getMain() != null) {
            float temperatureCelsius = response.getMain().getTemp_max() - 273.15F;

            if (temperatureCelsius > thresholds.getTemperatureThreshold()) {
                breachCount++;

                if (breachCount >= thresholds.getConsecutiveBreaches()) {
                    triggerAlert(city, temperatureCelsius, threshold);
                } else {
                    this.alertMessage = "No threshold breach detected!";
                }
            } else {
                breachCount = 0;
                this.alertMessage = "No threshold breach detected!";
            }
        }
        this.alertMessage = Constants.errorMessage;
    }

    private Optional<AlertConfig> getAlertConfigFromDb() {
        return configRepository.findByEmail(Constants.EMAIL);
    }

    private void triggerAlert(String city, float temperature, int threshold) {
        this.alertMessage = String.format("Alert! %s temperature exceeded threshold of %d°C : %.2f°C", city, threshold, temperature);
        System.out.println(alertMessage);
        sendEmailAlert(this.alertMessage, "Weather Alert");
        breachCount = 0;
        lastAlertTime = LocalDateTime.now(); // Update last alert time
    }

    public void sendEmailAlert(String body, String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(Constants.EMAIL);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(fromEmailId);

        javaMailSender.send(message);
    }


    @Transactional
    private void persistWeatherData(List<ResponseModel> responseModels) {
        responseModels.forEach(
                responseModel -> {
                    responseRepository.save(responseModel);
                }
        );
    }

    public String formatTemperature(float kelvinTemp) {
        float celsiusTemp = kelvinTemp - 273.15F;
        return String.format("%.2f", celsiusTemp);
    }

    public static float getAvgTemp(float maxTemp, float minTemp) {
        return (maxTemp + minTemp) / 2;
    }
}
