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
import org.dronedudes.backend.item.Item;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
    }

    public Agv saveAgvToDatabase(Agv agv) {
        agvMap.put(agv.getUuid(), agv);
        notifyChange(agv.getUuid());
        return agvRepository.save(agv);
    }
    public Optional<Agv> returnSingleAgv() {
        return agvRepository.findFirstByOrderById();
    }

    public String getAgvStatusFromSimulation(Agv agv) {
        try {
            return restTemplate.getForEntity(agv.getEndpointUrl(), String.class).getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Scheduled(fixedDelay = 1000)
    public boolean pollAgvSimulation() {
        for (Agv agv : agvMap.values()) {
            String agvJson = getAgvStatusFromSimulation(agv);
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
        return agvState != comparisonAgv.getAgvState();
    }

    @Override
    public void notifyChange(UUID machineId) {
        observerService.updateSubscribers(machineId);
    }


    /** AGV COMMAND METHODS
     *
     */
    public HttpHeaders getHeadersForPutCommand() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public boolean loadAndExecutePutCommand(Agv agv, String command) {
        try {
            String endpointUrl = agv.getEndpointUrl();
            HttpHeaders headers = getHeadersForPutCommand();
            Map<String, String> postParameters = new HashMap<>();
            postParameters.put("Program name", command);
            postParameters.put("State", "1");
            ObjectMapper mapper = new ObjectMapper();
            String jsonParams = mapper.writeValueAsString(postParameters);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonParams, headers);
            restTemplate.exchange(endpointUrl, HttpMethod.PUT, requestEntity, Void.class);

            postParameters.clear();
            postParameters.put("State", "2");
            requestEntity = new HttpEntity<>(jsonParams, headers);
            restTemplate.exchange(endpointUrl, HttpMethod.PUT, requestEntity, Void.class);
            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean agvMoveToAssemblyStation(UUID agvMachineId, UUID assemblyStationMachineId) {
        Agv agv = agvMap.get(agvMachineId);
        loadAndExecutePutCommand(agv, "MoveToAssemblyOperation");

        //Update the observer
        notifyChange(agv.getUuid());

        System.out.println(agv.getName() + " with Id: " + agvMachineId + " is moving to Assembly Station with Id: " + assemblyStationMachineId);
        return true;
    }

    public boolean agvMoveToWarehouse(UUID agvMachineId, UUID warehouseMachineId) {
        Agv agv = agvMap.get(agvMachineId);
        loadAndExecutePutCommand(agv, "MoveToStorageOperation");

        //Update the observer
        notifyChange(agv.getUuid());

        System.out.println(agv.getName() + " with Id: " + agvMachineId + " is moving to Warehouse with Id: " + warehouseMachineId);
        return true;
    }

    public boolean agvPickUpItemFromAssemblyStation(UUID agvMachineId, UUID assemblyStationMachineId, Item item) {
        Agv agv = agvMap.get(agvMachineId);
        loadAndExecutePutCommand(agv, "PickAssemblyOperation");
        agv.setInventory(item);

        //Update the observer
        notifyChange(agv.getUuid());

        System.out.println(agv.getName() + " with Id: " + agvMachineId + " picked up item " + item.getName() + " with Id: " + item.getId() + " from Assembly Station with Id: " + assemblyStationMachineId);
        return true;
    }

    public boolean agvPutItemOnAssemblyStation(UUID agvMachineId, UUID assemblyStationMachineId) {
        Agv agv = agvMap.get(agvMachineId);
        loadAndExecutePutCommand(agv, "PutAssemblyOperation");
        Item item = agv.getInventory();
        agv.setInventory(null);

        //Update the observer
        notifyChange(agv.getUuid());

        System.out.println(agv.getName() + " with Id: " + agvMachineId + " put item " + item.getName() + " with Id: " + item.getId() + " into Assembly Station with Id: " + assemblyStationMachineId);
        return true;
    }

    public boolean agvPickUpItemFromWarehouse(UUID agvMachineId, UUID warehouseMachineId, Item item) {
        Agv agv = agvMap.get(agvMachineId);
        loadAndExecutePutCommand(agv, "PickWarehouseOperation");
        agv.setInventory(item);

        //Update the observer
        notifyChange(agv.getUuid());

        System.out.println(agv.getName() + " with Id: " + agvMachineId + " picked up item " + item.getName() + " with Id: " + item.getId() + " from Warehouse with Id: " + warehouseMachineId);
        return true;
    }

    public boolean agvPutItemIntoWarehouse(UUID agvMachineId, UUID warehouseMachineId) {
        Agv agv = agvMap.get(agvMachineId);
        loadAndExecutePutCommand(agv, "PutWarehouseOperation");
        Item item = agv.getInventory();
        agv.setInventory(null);

        //Update the observer
        notifyChange(agv.getUuid());

        System.out.println(agv.getName() + " with Id: " + agvMachineId + " put item " + item.getName() + " with Id: " + item.getId() + " into Warehouse with Id: " + warehouseMachineId);
        return true;
    }

    public boolean agvMoveToChargingStation(UUID agvMachineId, UUID chargerId) {
        Agv agv = agvMap.get(agvMachineId);
        loadAndExecutePutCommand(agv, "MoveToChargerOperation");

        //Update the observer
        notifyChange(agv.getUuid());

        System.out.println(agv.getName() + " with Id: " + agvMachineId + " is moving to Charger with Id: " + chargerId);
        return true;
    }
}
