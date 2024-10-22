package com.example.WeatherAPP.servicetest;

import com.example.WeatherAPP.service.impl.WeatherServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailAlertTest {

    @InjectMocks
    private WeatherServiceImpl weatherService;

    @Mock
    private JavaMailSender javaMailSender;

    @Test
    public void testSendEmailAlert() {
        weatherService.sendEmailAlert("Test Alert", "Weather Alert");

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
