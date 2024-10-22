package com.example.WeatherAPP.servicetest;

import com.example.WeatherAPP.config.AlertConfig;
import com.example.WeatherAPP.constant.Constants;
import com.example.WeatherAPP.model.Main;
import com.example.WeatherAPP.model.ResponseModel;
import com.example.WeatherAPP.model.Weather;
import com.example.WeatherAPP.model.WeatherModel;
import com.example.WeatherAPP.repository.ConfigRepository;
import com.example.WeatherAPP.repository.ResponseRepository;
import com.example.WeatherAPP.service.impl.WeatherServiceImpl;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class WeatherServiceTest {

    @Mock
    private ConfigRepository configRepository;

    @InjectMocks
    private WeatherServiceImpl weatherService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ResponseRepository responseRepository;

    @Mock
    private JavaMailSender javaMailSender;

    @Test
    public void testContextLoads() {
        assertNotNull(weatherService);
    }

    @Test
    public void testGetCurrentWeather() {
        WeatherModel mockWeatherModel = new WeatherModel();

        // Mock the "weather" field inside WeatherModel
        ArrayList<Weather> mockWeatherList = new ArrayList<>();
        Weather mockWeather = new Weather();
        mockWeather.setDescription("Clear sky");
        mockWeatherList.add(mockWeather);

        // Mock the "main" field inside WeatherModel
        Main main = new Main();
        main.setTemp_max(310.15F); // Mock max temperature in Kelvin
        main.setTemp_min(295.15F); // Mock min temperature in Kelvin
        mockWeatherModel.setMain(main);
        mockWeatherModel.setWeather(mockWeatherList);

        // Mock the ConfigRepository and other dependencies
        AlertConfig mockConfig = new AlertConfig();
        mockConfig.setCity("Delhi");
        mockConfig.setThreshold(35); // Threshold in Celsius

        when(configRepository.findByEmail(Constants.EMAIL)).thenReturn(Optional.of(mockConfig));
        when(restTemplate.getForObject(anyString(), eq(WeatherModel.class))).thenReturn(mockWeatherModel);

        // Act
        List<ResponseModel> result = weatherService.getCurrentWeather();

        // Assert
        assertNotNull(result);
        assertEquals("Delhi", result.get(0).getCity());
        assertEquals("Clear sky", result.get(0).getDominantCondition());


    }

    @Test
    public void testKelvinToCelsiusConversion() {
        float kelvinTemp = 300.0F;
        String celsiusTemp = weatherService.formatTemperature(kelvinTemp);

        assertEquals("26.85", celsiusTemp);
    }

    @Test
    public void testDailySummary() {
        float maxTemp = 310.0F;
        float minTemp = 290.0F;

        float avgTemp = WeatherServiceImpl.getAvgTemp(maxTemp, minTemp);

        assertEquals(300.0F, avgTemp, 0.1);
    }

    @Test
    public void testTriggerAlert() {
        WeatherModel mockWeatherModel = new WeatherModel();

        // Mock the "weather" field inside WeatherModel
        ArrayList<Weather> mockWeatherList = new ArrayList<>();
        Weather mockWeather = new Weather();
        mockWeather.setDescription("Clear sky");
        mockWeatherList.add(mockWeather);

        // Ensure the list is not empty
        assertFalse(mockWeatherList.isEmpty()); // This checks the list is populated

        // Mock the "main" field inside WeatherModel
        Main main = new Main();
        main.setTemp_max(310.15F); // Mock max temperature in Kelvin
        main.setTemp_min(295.15F); // Mock min temperature in Kelvin
        mockWeatherModel.setMain(main);
        mockWeatherModel.setWeather(mockWeatherList); // Set weather list

        // Mock the ConfigRepository and other dependencies
        AlertConfig mockConfig = new AlertConfig();
        mockConfig.setCity("Delhi");
        mockConfig.setThreshold(35); // Threshold in Celsius

        when(configRepository.findByEmail(Constants.EMAIL)).thenReturn(Optional.of(mockConfig));
        when(restTemplate.getForObject(anyString(), eq(WeatherModel.class))).thenReturn(mockWeatherModel);

        // Act
        List<ResponseModel> result = weatherService.getCurrentWeather();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty()); // Ensure the result is not empty
        assertEquals("Delhi", result.get(0).getCity());
        assertEquals("Clear sky", result.get(0).getDominantCondition());


    }

}
