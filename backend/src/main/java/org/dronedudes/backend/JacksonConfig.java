/*package org.dronedudes.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.dronedudes.backend.Part.Part;
import org.dronedudes.backend.Part.PartJsonSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Part.class, new PartJsonSerializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }
}
*/