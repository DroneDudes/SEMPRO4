package org.dronedudes.backend;

import org.dronedudes.backend.agv.Agv;
import org.dronedudes.backend.agv.AgvRepository;
import org.dronedudes.backend.agv.AgvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class BackendApplication {
	@Autowired
	AgvRepository agvRepository;

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
