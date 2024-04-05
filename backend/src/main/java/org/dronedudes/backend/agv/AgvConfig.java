package org.dronedudes.backend.agv;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AgvConfig {
    @Bean
    CommandLineRunner initDatabase(AgvRepository agvRepository) {
        return args -> {
            agvRepository.save(new Agv(100, "no program loaded", 1));
        };
    }
}
