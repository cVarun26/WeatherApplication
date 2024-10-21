package com.example.WeatherAPP.service;

import org.springframework.stereotype.Service;

@Service
public interface AlertService {
    boolean setAlert(String city, int threshold);
}
