package com.example.WeatherAPP.model;

import lombok.Data;

@Data
public class Weather {
    private String description;
    private int id;
    private String main;
    private String icon;
}
