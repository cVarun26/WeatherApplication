package com.example.WeatherAPP.controller;

import com.example.WeatherAPP.model.ResponseModel;
import com.example.WeatherAPP.service.AlertService;
import com.example.WeatherAPP.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/setAlert")
    public ResponseEntity<Boolean> setAlertConfig(@RequestParam String city, @RequestParam int threshold){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(alertService.setAlert(city,threshold));
    }

    @GetMapping("/visualize")
    public ResponseEntity<String> getViz(){
      return null;
    }
}
