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
import org.dronedudes.backend.common.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
@Getter
@Transactional
public class AgvService implements PublisherInterface, IAgvService, SubscriberInterface {
    private final AgvRepository agvRepository;
    private Map<UUID, Agv> agvMap = new HashMap<>();

    private final ObserverService observerService;
    private final RestTemplate restTemplate = new RestTemplate();
    private Map<UUID, CountDownLatch> agvLatchMap = new HashMap<>();

    @PostConstruct
    public void fetchAllSystemAgvs() {
        saveAgvToDatabase(new Agv("Storeroom AGV", "http://localhost:8082/v1/status/"));
        subscribe();
    }

    public Agv saveAgvToDatabase(Agv agv) {
        agvMap.put(agv.getUuid(), agv);
        notifyChange(agv.getUuid());
        return agvRepository.save(agv);
    }

    public Optional<Agv> returnSingleAgv() {
        return agvRepository.findFirstByOrderById();
    }

    public String retrieveAgvStatus(Agv agv) {
        try {
            return restTemplate.getForEntity(agv.getEndpointUrl(), String.class).getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Scheduled(fixedDelay = 1000)
    public boolean pollAllAgvForStatusUpdates() {
        for (Agv agv : agvMap.values()) {
            String agvJson = retrieveAgvStatus(agv);
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
                System.out.println("notify called for " + agv.getName() + " with Id: " + agv.getUuid());

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

    /** AGV COMMAND METHODS */
    public HttpHeaders getHeadersForPutCommand() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public boolean loadAndExecutePutCommand(Agv agv, String command) {
        try {
            String endpointUrl = agv.getEndpointUrl();
            HttpHeaders headers = getHeadersForPutCommand();
            ObjectMapper mapper = new ObjectMapper();

            // Prepare the first PUT request to set the command
            Map<String, String> postParameters = new HashMap<>();
            postParameters.put("Program name", command);
            postParameters.put("State", "1");
            String jsonParams = mapper.writeValueAsString(postParameters);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonParams, headers);

            // Initialize latch for the program change
            CountDownLatch programLatch = new CountDownLatch(1);
            agvLatchMap.put(agv.getUuid(), programLatch);

            // Send the first PUT request
            restTemplate.exchange(endpointUrl, HttpMethod.PUT, requestEntity, Void.class);

            // Wait for the program to change
            waitForAgvState(agv.getUuid(), programLatch);

            // Prepare the second PUT request to execute the command
            postParameters.clear();
            postParameters.put("State", "2");
            jsonParams = mapper.writeValueAsString(postParameters);
            requestEntity = new HttpEntity<>(jsonParams, headers);

            // Initialize latch for the state change (IDLE -> EXECUTING -> IDLE)
            CountDownLatch stateLatch = new CountDownLatch(1);
            agvLatchMap.put(agv.getUuid(), stateLatch);
            System.out.println("sending execute command to " + agv.getName() + " with Id: " + agv.getUuid());

            // Send the second PUT request
            System.out.println(restTemplate.exchange(endpointUrl, HttpMethod.PUT, requestEntity, Void.class));

            // Wait for the AGV to become idle again after executing the command
            waitForAgvState(agv.getUuid(), stateLatch);

            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private void waitForAgvState(UUID agvUuid, CountDownLatch latch) throws InterruptedException, TimeoutException {
        System.out.println("Waiting for AGV with Id: " + agvUuid + " to reach the desired state");
        if (!latch.await(10, TimeUnit.SECONDS)) {

        }
    }

    @Override
    public boolean agvMoveToAssemblyStation(UUID agvMachineId, UUID assemblyStationMachineId) {
        Agv agv = agvMap.get(agvMachineId);
        loadAndExecutePutCommand(agv, "MoveToAssemblyOperation");

        // Update the observer
        notifyChange(agv.getUuid());

        System.out.println(agv.getName() + " with Id: " + agvMachineId + " is moving to Assembly Station with Id: " + assemblyStationMachineId);
        return true;
    }

    @Override
    public boolean agvMoveToWarehouse(UUID agvMachineId, UUID warehouseMachineId) {
        Agv agv = agvMap.get(agvMachineId);
        loadAndExecutePutCommand(agv, "MoveToStorageOperation");

        // Update the observer
        notifyChange(agv.getUuid());

        System.out.println(agv.getName() + " with Id: " + agvMachineId + " is moving to Warehouse with Id: " + warehouseMachineId);
        return true;
    }

    @Override
    public boolean agvPickUpItemFromAssemblyStation(UUID agvMachineId, UUID assemblyStationMachineId, Item item) {
        Agv agv = agvMap.get(agvMachineId);
        loadAndExecutePutCommand(agv, "PickAssemblyOperation");
        agv.setInventory(item);

        // Update the observer
        notifyChange(agv.getUuid());

        System.out.println(agv.getName() + " with Id: " + agvMachineId + " picked up item " + item.getName() + " with Id: " + item.getId() + " from Assembly Station with Id: " + assemblyStationMachineId);
        return true;
    }

    @Override
    public boolean agvPutItemOnAssemblyStation(UUID agvMachineId, UUID assemblyStationMachineId) {
        Agv agv = agvMap.get(agvMachineId);
        loadAndExecutePutCommand(agv, "PutAssemblyOperation");
        Item item = agv.getInventory();
        agv.setInventory(null);

        // Update the observer
        notifyChange(agv.getUuid());

        System.out.println(agv.getName() + " with Id: " + agvMachineId + " put item " + item.getName() + " with Id: " + item.getId() + " into Assembly Station with Id: " + assemblyStationMachineId);
        return true;
    }

    @Override
    public boolean agvPickUpItemFromWarehouse(UUID agvMachineId, UUID warehouseMachineId, Item item) {
        Agv agv = agvMap.get(agvMachineId);
        loadAndExecutePutCommand(agv, "PickWarehouseOperation");
        agv.setInventory(item);

        // Update the observer
        notifyChange(agv.getUuid());

        System.out.println(agv.getName() + " with Id: " + agvMachineId + " picked up item " + item.getName() + " with Id: " + item.getId() + " from Warehouse with Id: " + warehouseMachineId);
        return true;
    }

    @Override
    public boolean agvPutItemIntoWarehouse(UUID agvMachineId, UUID warehouseMachineId) {
        Agv agv = agvMap.get(agvMachineId);
        loadAndExecutePutCommand(agv, "PutWarehouseOperation");
        Item item = agv.getInventory();
        agv.setInventory(null);

        // Update the observer
        notifyChange(agv.getUuid());

        System.out.println(agv.getName() + " with Id: " + agvMachineId + " put item " + item.getName() + " with Id: " + item.getId() + " into Warehouse with Id: " + warehouseMachineId);
        return true;
    }

    @Override
    public boolean agvMoveToChargingStation(UUID agvMachineId) {
        Agv agv = agvMap.get(agvMachineId);
        loadAndExecutePutCommand(agv, "MoveToChargerOperation");

        // Update the observer
        notifyChange(agv.getUuid());

        System.out.println(agv.getName() + " with Id: " + agvMachineId + " is moving to Charger with Id: ??");
        return true;
    }

    @Override
    public UUID getAvailableAgv() {
        if (returnSingleAgv().isEmpty()) {
            return null;
        }
        return returnSingleAgv().get().getUuid();
    }

    @Override
    public AgvStateEnum getAgvState(UUID agvMachineId) {
        return agvMap.get(agvMachineId).getAgvState();
    }

    public boolean isAgvIdle(UUID agvMachineId) {
        return getAgvState(agvMachineId).equals(AgvStateEnum.IDLE_STATE);
    }

    @Override
    public void subscribe() {
        for (Agv agv : agvMap.values()) {
            observerService.subscribe(agv.getUuid(), this);
        }
    }

    @Override
    public void update(UUID machineId) {
        System.out.println("AgvService received an update for " + machineId);
        if (agvLatchMap.containsKey(machineId)) {
            if(agvMap.get(machineId).getAgvState() == AgvStateEnum.IDLE_STATE) {
                agvLatchMap.get(machineId).countDown();
            }
        }
    }
}
