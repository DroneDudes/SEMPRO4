package org.dronedudes.backend.agv;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
@Transactional
public class AgvLogEntryService {
    private final AgvLogEntryRepository agvLogEntryRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedDelay = 1000)
    public void scheduledAGVLog() {
        String agvJson = restTemplate.getForEntity("http://localhost:8082/v1/status/", String.class).getBody();
        try {
            JsonNode agvNode = new ObjectMapper().readTree(agvJson);
            int battery = agvNode.get("battery").intValue();
            String programName = agvNode.get("program name").textValue();
            System.out.println(programName);
            String state = agvNode.get("state").textValue();
            Agv fetchedAGV = new Agv("Warehouse AGV");
            Optional<AgvProgramEnum> agvProgram = AgvProgramEnum.getAgvProgramByValue(programName);
            Optional<AgvStateEnum> agvState = AgvStateEnum.getAgvStateByValue(state);
            System.out.println(battery);
            if (agvProgram.isEmpty()) {
                System.out.println(agvProgram);
                throw new Exception("No program was found by that name");
            }
            if (agvState.isEmpty()) {
                throw new Exception("No program was found by that name");
            }
            agvLogEntryRepository.save(new AgvLogEntry(battery, agvProgram.get(), agvState.get(), fetchedAGV));

        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
