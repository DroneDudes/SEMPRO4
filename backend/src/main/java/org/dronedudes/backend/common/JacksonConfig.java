package org.dronedudes.backend.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.dronedudes.backend.common.IBlueprint;
import org.dronedudes.backend.common.IBlueprintDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(IBlueprint.class, new IBlueprintDeserializer());
        mapper.registerModule(module);
        return mapper;
    }
}
