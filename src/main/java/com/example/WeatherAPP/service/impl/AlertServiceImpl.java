package com.example.WeatherAPP.service.impl;

import com.example.WeatherAPP.config.AlertConfig;
import com.example.WeatherAPP.constant.Constants;
import com.example.WeatherAPP.model.AlertThresholds;
import com.example.WeatherAPP.model.WeatherModel;
import com.example.WeatherAPP.repository.ConfigRepository;
import com.example.WeatherAPP.service.AlertService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AlertServiceImpl implements AlertService {

    @Autowired
    private ConfigRepository configRepository;
    @Override
    @Transactional
    public boolean setAlert(String city, int threshold) {
        Optional<AlertConfig> alertConfigOptional = configRepository.findByEmail(Constants.EMAIL);

        if(alertConfigOptional.isPresent()) {

            AlertConfig alertConfig = alertConfigOptional.get();
            alertConfig.setCity(city);
            alertConfig.setThreshold(threshold);
            configRepository.save(alertConfig);
        } else {
            configRepository.save(new AlertConfig(city, Constants.EMAIL, threshold));
        }
        return true;
    }
}
