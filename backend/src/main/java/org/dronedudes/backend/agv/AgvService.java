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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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


    /** AGV COMMAND METHODS
     *
     */
    public boolean agvMoveToAssemblyStation(UUID agvId, UUID destinationMachineId) {
        Agv agv = agvMap.get(agvId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //Load command
        MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("Program name", "MoveToAssemblyOperation");
        postParameters.add("State", "1");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(postParameters, headers);
        restTemplate.exchange(agv.getEndpointUrl(), HttpMethod.PUT, requestEntity, Void.class);

        //Execute command
        postParameters.clear();
        postParameters.add("State", "2");
        restTemplate.exchange(agv.getEndpointUrl(), HttpMethod.PUT, requestEntity, Void.class);

        //Update the observer
        notifyChange(agv.getUuid());

        return false;
    }

    public boolean agvMoveToWarehouse(UUID agvMachineId, UUID destinationMachineId) {
        return false;
    }

    public boolean agvPickUpItemFromAssemblyStation(UUID agvMachineId, Long itemId) {
        return false;
    }

    public boolean agvPutDownItemOnAssemblyStation(UUID agvMachineId) {
        return false;
    }

    public boolean agvPickUpItemFromWarehouse(UUID agvMachineId, Long itemId) {
        return false;
    }

    public boolean agvPutDownItemInWarehouse(UUID agvMachineId) {
        return false;
    }

    public boolean agvMoveToChargingStation(UUID agvMachineId) {
        return false;
    }
}
