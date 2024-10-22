package com.example.WeatherAPP.service;

import com.example.WeatherAPP.model.ResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface WeatherService {

    List<ResponseModel> getCurrentWeather();
}
