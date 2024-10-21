package com.example.WeatherAPP.config;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AlertConfig {
    private String city;
    @Id
    private String email;
    private int threshold;
}
