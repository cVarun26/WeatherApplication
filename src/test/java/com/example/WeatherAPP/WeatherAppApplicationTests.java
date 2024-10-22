package com.example.WeatherAPP;

import com.example.WeatherAPP.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class WeatherAppApplicationTests {

	@Autowired
	private WeatherService weatherService;

	@Test
	void contextLoads() {
		assertNotNull(weatherService);
	}

}
