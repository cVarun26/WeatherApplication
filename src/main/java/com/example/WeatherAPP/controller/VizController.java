package com.example.WeatherAPP.controller;

import com.example.WeatherAPP.model.VizModel;
import com.example.WeatherAPP.service.VizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/viz")
public class VizController {

    @Autowired
    private VizService vizService;

    @GetMapping("/{city}")
    public ResponseEntity<VizModel> getViz(@PathVariable String city){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(vizService.getAvgTempsForCity(city));
    }
}
