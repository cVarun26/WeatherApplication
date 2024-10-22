package com.example.WeatherAPP.controllertest;

import com.example.WeatherAPP.controller.WeatherController;
import com.example.WeatherAPP.model.ResponseModel;
import com.example.WeatherAPP.service.AlertService;
import com.example.WeatherAPP.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeatherControllerTest {


    @Mock
    private WeatherService weatherService;

    @Mock
    private AlertService alertService;

    @InjectMocks
    private WeatherController weatherController;

    @Test
    public void testGetCurrentWeather(){
        List<ResponseModel> mockWeatherData=new ArrayList<>();

        when(weatherService.getCurrentWeather()).thenReturn(mockWeatherData);

        ResponseEntity<List<ResponseModel>> response=weatherController.getCurrentWeather();
        assertEquals(200,response.getStatusCodeValue());
        assertEquals(mockWeatherData,response.getBody());
    }

    @Test
    public void testSertAlertConfig(){
        String city="Bangalore";
        int threshold=20;

        when(alertService.setAlert(city,threshold)).thenReturn(true);

        ResponseEntity<Boolean> response=weatherController.setAlertConfig(city,threshold);
        assertEquals(200,response.getStatusCodeValue());
        assertEquals(true,response.getBody());
    }







}
