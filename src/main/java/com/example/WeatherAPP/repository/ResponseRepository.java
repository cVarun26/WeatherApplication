package com.example.WeatherAPP.repository;

import com.example.WeatherAPP.model.ResponseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseRepository extends JpaRepository<ResponseModel, Long> {
}
