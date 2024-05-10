package org.dronedudes.backend.agv;

import org.dronedudes.backend.BackendApplication;
import org.dronedudes.backend.Part.Part;
import org.dronedudes.backend.item.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

@SpringBootTest
@ContextConfiguration(classes = BackendApplication.class)
class AgvServiceTest {

    Agv agv1 = new Agv("Storeroom AGV", "http://localhost:8082/v1/status/");
    Agv agv2 = new Agv("Frontroom AGV", "http://localhost:8082/v1/status/");
    Item part1 = new Part("Part1", "A part for testing purposes", "test", "test", 500);

    @Autowired
    private AgvService agvService;
    @Autowired
    private AgvRepository agvRepository;

    @BeforeEach
    void setUp() {
        agvService.saveAgvToDatabase(agv1);
        System.out.println(agv1.getUuid());
        agvService.saveAgvToDatabase(agv2);
        System.out.println(agv2.getUuid());
    }

    @AfterEach
    void tearDown() {
        agvRepository.deleteAll();
        agvService.getAgvMap().clear();
    }

    @Test
    void localAgvMapContainsCorrectAmountOfAgvs() {
        assert agvService.getAgvMap().entrySet().size() == 2;
    }

    @Test
    void databaseContainsCorrectAmountOfAgvs() {
        assert agvRepository.findAll().size() == 2;
    }

    @Test
    void localMapSizeMatchesDatabaseSize() {
        System.out.println(agvService.getAgvMap().size());
        assert agvRepository.findAll().size() == agvService.getAgvMap().size();
    }

    @Test
    void pollAgvSimulation() {
        assert agvService.pollAgvSimulation();
    }

    @Test
    void agvCanLoadAndExecuteChargeProgram() {
        agvService.agvMoveToChargingStation(agv1.getUuid(), new UUID(0, 0));
        assert agvService.getAgvStatusFromSimulation(agv1).contains("MoveToChargerOperation");
    }

    @Test
    void agvCanLoadAndExecuteMoveToAssemblyProgram() {
        agvService.agvMoveToAssemblyStation(agv1.getUuid(), new UUID(0, 0));
        assert agvService.getAgvStatusFromSimulation(agv1).contains("MoveToAssemblyOperation");
    }

    @Test
    void agvCanLoadAndExecuteMoveToWarehouseProgram() {
        agvService.agvMoveToWarehouse(agv1.getUuid(), new UUID(0, 0));
        assert agvService.getAgvStatusFromSimulation(agv1).contains("MoveToStorageOperation");
    }

    @Test
    void agvCanLoadAndExecutePickUpItemFromAssemblyStationProgram() {
        agvService.agvPickUpItemFromAssemblyStation(agv1.getUuid(), new UUID(0, 0), part1);
        assert agvService.getAgvStatusFromSimulation(agv1).contains("PickAssemblyOperation");
        assert agv1.getInventory().equals(part1);
    }

    @Test
    void agvCanLoadAndExecutePutItemOnAssemblyStationProgram() {
        agv1.setInventory(part1);
        agvService.agvPutItemOnAssemblyStation(agv1.getUuid(), new UUID(0, 0));
        assert agvService.getAgvStatusFromSimulation(agv1).contains("PutAssemblyOperation");
        assert agv1.getInventory() == null;
    }

    @Test
    void agvCanLoadAndExecutePickUpItemFromWarehouseProgram() {
        agvService.agvPickUpItemFromWarehouse(agv1.getUuid(), new UUID(0, 0), part1);
        assert agvService.getAgvStatusFromSimulation(agv1).contains("PickWarehouseOperation");
        assert agv1.getInventory().equals(part1);
    }

    @Test
    void agvCanLoadAndExecutePutItemIntoWarehouseProgram() {
        agv1.setInventory(part1);
        agvService.agvPutItemIntoWarehouse(agv1.getUuid(), new UUID(0, 0));
        assert agvService.getAgvStatusFromSimulation(agv1).contains("PutWarehouseOperation");
        assert agv1.getInventory() == null;
    }
}