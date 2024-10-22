package com.example.WeatherAPP.repository;

import com.example.WeatherAPP.model.ResponseModel;
import com.example.WeatherAPP.model.VizModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VizRepository extends JpaRepository<ResponseModel,String > {

    Optional<List<ResponseModel>> findByCity(String city);
}
