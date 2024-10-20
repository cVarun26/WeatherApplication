package com.example.WeatherAPP.model;

import lombok.Data;

@Data
public class AlertThresholds {
    private float temperatureThreshold;
    private int consecutiveBreaches;
}
