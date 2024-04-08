package org.dronedudes.backend.agv.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dronedudes.backend.agv.Agv;
import org.dronedudes.backend.agv.program.AgvProgramEnum;
import org.dronedudes.backend.agv.AgvRepository;
import org.dronedudes.backend.agv.state.AgvStateEnum;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Getter
@Transactional
public class AgvLogEntryService {
    private final AgvLogEntryRepository agvLogEntryRepository;
    private final AgvRepository agvRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedDelay = 60000)
    public void scheduledAGVLog() {
        String agvJson = restTemplate.getForEntity("http://localhost:8082/v1/status/", String.class).getBody();
        try {
            JsonNode agvNode = new ObjectMapper().readTree(agvJson);
            int battery = agvNode.get("battery").intValue();
            String programName = agvNode.get("program name").textValue();
            int state = agvNode.get("state").intValue();
            Agv fetchedAGV = new Agv("Warehouse AGV");
            agvRepository.save(fetchedAGV);
            AgvProgramEnum agvProgram = AgvProgramEnum.find(programName);
            System.out.println("State: " + state);
            AgvStateEnum agvState = AgvStateEnum.find(state);
            if (agvProgram == null) {
                throw new Exception("No program was found by that name");
            }
            if (agvState == null) {
                throw new Exception("No state was found by that name");
            }
            agvLogEntryRepository.save(new AgvLogEntry(battery, agvProgram.getProgramName(), agvState.getState(), fetchedAGV));

        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
