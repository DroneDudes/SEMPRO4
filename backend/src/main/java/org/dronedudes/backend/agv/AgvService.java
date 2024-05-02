package org.dronedudes.backend.agv;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dronedudes.backend.agv.program.AgvProgramEnum;
import org.dronedudes.backend.agv.state.AgvStateEnum;
import org.dronedudes.backend.common.ObserverService;
import org.dronedudes.backend.common.PublisherInterface;
import org.dronedudes.backend.common.SsePublisherInterface;
import org.dronedudes.backend.common.logging.LogEntry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
@Getter
@Transactional
public class AgvService implements PublisherInterface {
    private final AgvRepository agvRepository;
    private Map<UUID, Agv> agvMap = new HashMap<>();

    private final ObserverService observerService;
    private final RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    public void fetchAllSystemAgvs() {
        saveAgvToDatabase(new Agv("Storeroom AGV", "http://localhost:8082/v1/status/"));
        System.out.println("CURRENT AGV MAP SIZE: " + agvMap.size());
    }

    public Agv saveAgvToDatabase(Agv agv) {
        agvMap.put(agv.getUuid(), agv);
        notifyChange(agv.getUuid());
        return agvRepository.save(agv);
    }
    public Optional<Agv> returnSingleAgv() {
        return agvRepository.findFirstByOrderById();
    }

    @Scheduled(fixedDelay = 1000)
    public boolean pollAgvSimulation() {
        for (Agv agv : agvMap.values()) {
            String agvJson = restTemplate.getForEntity(agv.getEndpointUrl(), String.class).getBody();
            try {
                JsonNode agvNode = new ObjectMapper().readTree(agvJson);
                int battery = agvNode.get("battery").intValue();
                String programName = agvNode.get("program name").textValue();
                int state = agvNode.get("state").intValue();

                AgvProgramEnum agvProgram = AgvProgramEnum.find(programName);
                AgvStateEnum agvState = AgvStateEnum.find(state);
                if (agvProgram == null) {
                    throw new Exception("No program was found by that name");
                }
                if (agvState == null) {
                    throw new Exception("No state was found by that name");
                }
                if (!agvIsChanged(battery, agvProgram, agvState, agv)) {
                    continue;
                }
                agv.setBattery(battery);
                agv.setAgvProgram(agvProgram);
                agv.setAgvState(agvState);

                notifyChange(agv.getUuid());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public boolean agvIsChanged(int battery, AgvProgramEnum agvProgram, AgvStateEnum agvState, Agv comparisonAgv) {
        if (battery != comparisonAgv.getBattery()) {
            return true;
        }
        if (agvProgram != comparisonAgv.getAgvProgram()) {
            return true;
        }
        if (agvState != comparisonAgv.getAgvState()) {
            return true;
        }
        return false;
    }

    @Override
    public void notifyChange(UUID machineId) {
        observerService.updateSubscribers(machineId);
    }

}
