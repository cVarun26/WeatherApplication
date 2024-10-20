package com.example.WeatherAPP.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class WeatherModel {
    Coord coord;
    ArrayList<Weather> weather = new ArrayList<>();
    private String base;
    Main main;
    private float visibility;
    Wind wind;
    Rain rain;
    Clouds clouds;
    private long dt;
    Sys sys;
    private float timezone;
    private float id;
    private String name;
    private float cod;
}
@Data
class Sys {
    private float type;
    private float id;
    private String country;
    private float sunrise;
    private float sunset;
}

@Data
class Clouds {
    private float all;
}
@Data
class Rain {
    @JsonProperty("1h")
    private float oneHour;
}
class Wind {
    private float speed;
    private float deg;
    private float gust;
}



@Data
class Coord {
    private float lon;
    private float lat;
}

