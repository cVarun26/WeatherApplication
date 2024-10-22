package com.example.WeatherAPP.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
public class VizModel {

    private List<String> avgTemps;

    @Id
    private String city;
    private List<LocalDateTime> timestamps;

    public VizModel() {
        this.avgTemps = new ArrayList<>();
        this.timestamps = new ArrayList<>();
    }
}
