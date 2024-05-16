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
import org.dronedudes.backend.common.IAgvService;
import org.dronedudes.backend.common.ObserverService;
import org.dronedudes.backend.common.PublisherInterface;
import org.dronedudes.backend.common.Item;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
@Getter
@Transactional
public class AgvService implements PublisherInterface, IAgvService {
    private final AgvRepository agvRepository;
    private Map<UUID, Agv> agvMap = new HashMap<>();

    private final ObserverService observerService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final Object agvStateLock = new Object();
    private CountDownLatch latch = new CountDownLatch(1);

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
                System.out.println(programName);
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
                if(agvState.equals(AgvStateEnum.EXECUTING_STATE) && latch.getCount()==2L){
                    latch.countDown();
                    System.out.println("Latch counted down. AGV state = " + agv.getAgvState() + "time: " + Timestamp.from(Instant.now()));
                }
                if(agvState.equals(AgvStateEnum.IDLE_STATE) && latch.getCount()==1L){
                    latch.countDown();
                    System.out.println("Latch counted down. AGV state = " + agv.getAgvState() + "time: " + Timestamp.from(Instant.now()));
                }
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
            AgvStateEnum agvStateEnum = agv.getAgvState();
            String endpointUrl = agv.getEndpointUrl();
            HttpHeaders headers = getHeadersForPutCommand();
            Map<String, String> postParameters = new HashMap<>();
            postParameters.put("Program name", command);
            postParameters.put("State", "1");
            ObjectMapper mapper = new ObjectMapper();
            String jsonParams = mapper.writeValueAsString(postParameters);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonParams, headers);
            Object o = null;
            o = restTemplate.exchange(endpointUrl, HttpMethod.PUT, requestEntity, Void.class);
            while (!agvStateEnum.equals(AgvStateEnum.IDLE_STATE)) {

            }
            agvStateEnum = agv.getAgvState();
            Instant now = Instant.now();
            Instant end = now.plus(1000, ChronoUnit.MILLIS);

            postParameters.clear();
            postParameters.put("State", "2");
            requestEntity = new HttpEntity<>(jsonParams, headers);
            Object o1 = null;
            o1 = restTemplate.exchange(endpointUrl, HttpMethod.PUT, requestEntity, Void.class);
            while (!agvStateEnum.equals(AgvStateEnum.IDLE_STATE)) {
            }
            agvStateEnum = agv.getAgvState();
            while (o1=="Already executing a task." || o1==null) {
            }
            Instant now1 = Instant.now();
            Instant end1 = now.plus(10000, ChronoUnit.MILLIS);

            while (Instant.now().isBefore(end1)) {
            }
            waitForAgvToBeIdle(agv.getUuid());
            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(fixedDelay = 10000)
    public void movetowh(){
        agvMoveToWarehouse(agvMap.values().stream().findFirst().get().getUuid(),new UUID(0,0));
    }

    @Override
    public boolean agvMoveToAssemblyStation(UUID agvMachineId, UUID assemblyStationMachineId) {
        Agv agv = agvMap.get(agvMachineId);
        loadAndExecutePutCommand(agv, "MoveToAssemblyOperation");

        //Update the observer
        notifyChange(agv.getUuid());

        System.out.println(agv.getName() + " with Id: " + agvMachineId + " is moving to Assembly Station with Id: " + assemblyStationMachineId);
        return true;
    }
    @Override
    public boolean agvMoveToWarehouse(UUID agvMachineId, UUID warehouseMachineId) {
        Agv agv = agvMap.get(agvMachineId);
        loadAndExecutePutCommand(agv, "MoveToStorageOperation");

        //Update the observer
        notifyChange(agv.getUuid());

        System.out.println(agv.getName() + " with Id: " + agvMachineId + " is moving to Warehouse with Id: " + warehouseMachineId);
        return true;
    }

    @Override
    public boolean agvPickUpItemFromAssemblyStation(UUID agvMachineId, UUID assemblyStationMachineId, Item item) {
        Agv agv = agvMap.get(agvMachineId);
        loadAndExecutePutCommand(agv, "PickAssemblyOperation");
        agv.setInventory(item);

        //Update the observer
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

        //Update the observer
        notifyChange(agv.getUuid());

        System.out.println(agv.getName() + " with Id: " + agvMachineId + " put item " + item.getName() + " with Id: " + item.getId() + " into Assembly Station with Id: " + assemblyStationMachineId);
        return true;
    }

    @Override
    public boolean agvPickUpItemFromWarehouse(UUID agvMachineId, UUID warehouseMachineId, Item item) {
        Agv agv = agvMap.get(agvMachineId);
        loadAndExecutePutCommand(agv, "PickWarehouseOperation");
        agv.setInventory(item);

        //Update the observer
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

        //Update the observer
        notifyChange(agv.getUuid());

        System.out.println(agv.getName() + " with Id: " + agvMachineId + " put item " + item.getName() + " with Id: " + item.getId() + " into Warehouse with Id: " + warehouseMachineId);
        return true;
    }

    @Override
    public boolean agvMoveToChargingStation(UUID agvMachineId) {
        Agv agv = agvMap.get(agvMachineId);
        loadAndExecutePutCommand(agv, "MoveToChargerOperation");

        //Update the observer
        notifyChange(agv.getUuid());

        System.out.println(agv.getName() + " with Id: " + agvMachineId + " is moving to Charger with Id: ??");
        return true;
    }

    @Override
    public UUID getAvailableAgv(){
        if(returnSingleAgv().isEmpty()){
            return null;
        }
        return returnSingleAgv().get().getUuid();
    }

    @Override
    public AgvStateEnum getAgvState(UUID agvMachineId){
        return agvMap.get(agvMachineId).getAgvState();
    }

    private void waitForAgvToBeIdle(UUID agvMachineId) throws InterruptedException, TimeoutException {
        latch = new CountDownLatch(2);

        synchronized (agvStateLock) {
            while (!isAgvIdle(agvMachineId)) {
                if (!latch.await(10, TimeUnit.SECONDS)) {
                    throw new TimeoutException("AGV did not become idle within the timeout period");
                }
            }
            System.out.println(getAgvState(agvMachineId) + " time: " + Timestamp.from(Instant.now()));
        }
    }
    public boolean isAgvIdle(UUID agvMachineId){
        return getAgvState(agvMachineId).equals(AgvStateEnum.IDLE_STATE);
    }
}