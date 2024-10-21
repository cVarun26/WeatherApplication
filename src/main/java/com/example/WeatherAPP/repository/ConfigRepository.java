package com.example.WeatherAPP.repository;

import com.example.WeatherAPP.config.AlertConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigRepository extends JpaRepository<AlertConfig, String> {

    Optional<AlertConfig> findByEmail(String email);
}
