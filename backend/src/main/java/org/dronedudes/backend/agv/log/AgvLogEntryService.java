package org.dronedudes.backend.agv.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dronedudes.backend.agv.Agv;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Getter
@Transactional
public class AgvLogEntryService {
    private final AgvLogEntryRepository agvLogEntryRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled()
    private void scheduledAGVLog() {
        String agvJson = restTemplate.getForEntity("http://localhost:8082/v1/status/", String.class).getBody();
        try {
            JsonNode agvNode = new ObjectMapper().readTree(agvJson);
            int battery = agvNode.get("battery").intValue();
            String programName = agvNode.get("program name").textValue();
            int state = agvNode.get("state").intValue();
            Agv fetchedAGV = new Agv()
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
