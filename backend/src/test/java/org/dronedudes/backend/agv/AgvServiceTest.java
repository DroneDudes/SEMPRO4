package org.dronedudes.backend.agv;

import org.dronedudes.backend.BackendApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = BackendApplication.class)
class AgvServiceTest {

    Agv agv1 = new Agv("Storeroom AGV", "http://localhost:8082/v1/status/");
    Agv agv2 = new Agv("Frontroom AGV", "http://localhost:8082/v1/status/");

    @Autowired
    private AgvService agvService;
    @Autowired
    private AgvRepository agvRepository;
    @Autowired
    private AgvObserverService agvObserverService;

    @BeforeEach
    void setUp() {
        agvService.saveAgvToDatabase(agv1);
        agvService.saveAgvToDatabase(agv2);
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
        assert agvRepository.findAll().size() == agvService.getAgvMap().size();
    }

    @Test
    void pollAgvSimulation() {
        assert agvService.pollAgvSimulation();
    }
}