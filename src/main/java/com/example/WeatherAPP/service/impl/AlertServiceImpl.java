package com.example.WeatherAPP.service.impl;

import com.example.WeatherAPP.model.AlertThresholds;
import com.example.WeatherAPP.model.WeatherModel;
import com.example.WeatherAPP.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

@Service
public class AlertServiceImpl implements AlertService {

    private static final AlertThresholds thresholds = new AlertThresholds();
    private static int breachCount = 2;
    private LocalDateTime lastAlertTime;

    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather";
    private static final String API_KEY = "6f12ae61b995ae19aa173a138291577c";

    private String alertMessage = "";

    private static final String errorMessage = "An unexpected error detected!";

    @Autowired
    private RestTemplate restTemplate;

    public AlertServiceImpl() {
        thresholds.setConsecutiveBreaches(breachCount);
    }

    @Override
    public String getWeatherAlert(String city, int threshold) {
        thresholds.setTemperatureThreshold(threshold);
        String url = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("q", city)
                .queryParam("appid", API_KEY)
                .build()
                .toUriString();

        WeatherModel response = restTemplate.getForObject(url, WeatherModel.class);
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
                return this.alertMessage;
            }

            return this.alertMessage;
        }
        return errorMessage;
    }

    private void triggerAlert(String city, float temperature, int threshold) {
        this.alertMessage = String.format("Alert! %s temperature exceeded threshold of %d°C : %.2f°C", city, threshold, temperature);
        System.out.println(alertMessage);
        breachCount = 0;
        // TODO You can add email sending logic here or any other alert mechanism
        lastAlertTime = LocalDateTime.now(); // Update last alert time
    }
}
