package com.example.WeatherAPP.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class ResponseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String city;
    private String dominantCondition;
    private String avgTemp;
    private String maxTemp;
    private String minTemp;
    private LocalDateTime timestamp;
}
