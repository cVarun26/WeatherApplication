package com.example.WeatherAPP.service;

import org.springframework.stereotype.Service;

@Service
public interface AlertService {
    String getWeatherAlert(String city, int threshold);
}
