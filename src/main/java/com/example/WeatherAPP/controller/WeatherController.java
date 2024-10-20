package com.example.WeatherAPP.controller;

import com.example.WeatherAPP.model.ResponseModel;
import com.example.WeatherAPP.service.AlertService;
import com.example.WeatherAPP.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private AlertService alertService;

    @GetMapping("/weather")
    public ResponseEntity<List<ResponseModel>> getCurrentWeather() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(weatherService.getCurrentWeather());
    }

    @GetMapping("/alerts")
    public ResponseEntity<String> getWeatherAlert(@RequestParam int threshold, @RequestParam String city) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(alertService.getWeatherAlert(city, threshold));
    }

}
