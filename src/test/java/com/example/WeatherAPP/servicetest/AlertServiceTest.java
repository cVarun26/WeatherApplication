package com.example.WeatherAPP.servicetest;

import com.example.WeatherAPP.config.AlertConfig;
import com.example.WeatherAPP.repository.ConfigRepository;
import com.example.WeatherAPP.service.impl.AlertServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AlertServiceTest {

    @InjectMocks
    private AlertServiceImpl alertService;

    @Mock
    private ConfigRepository configRepository;

    @Test
    public void testSetAlert() {
        String city = "Bangalore";
        int threshold = 35;

        when(configRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        boolean result = alertService.setAlert(city, threshold);

        assertTrue(result);
        verify(configRepository, times(1)).save(any(AlertConfig.class));
    }

    @Test
    public void testUpdateAlertConfig() {
        String city = "Bangalore";
        int threshold = 35;
        AlertConfig mockConfig = new AlertConfig(city, "test@example.com", 30);

        when(configRepository.findByEmail(anyString())).thenReturn(Optional.of(mockConfig));

        boolean result = alertService.setAlert(city, threshold);

        assertTrue(result);
        verify(configRepository, times(1)).save(mockConfig);
        assertEquals(threshold, mockConfig.getThreshold());
    }
}
