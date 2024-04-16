package org.dronedudes.backend.agv;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AgvConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
