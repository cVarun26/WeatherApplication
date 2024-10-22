package com.example.WeatherAPP.service;

import com.example.WeatherAPP.model.VizModel;

public interface VizService {
    VizModel getAvgTempsForCity(String city);
}
