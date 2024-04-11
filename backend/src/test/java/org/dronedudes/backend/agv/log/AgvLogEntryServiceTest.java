package org.dronedudes.backend.agv.log;

import org.dronedudes.backend.BackendApplication;
import org.dronedudes.backend.agv.Agv;
import org.dronedudes.backend.agv.AgvObserverService;
import org.dronedudes.backend.agv.AgvRepository;
import org.dronedudes.backend.agv.AgvService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = BackendApplication.class)
class AgvLogEntryServiceTest {

    Agv agv1 = new Agv("Storeroom AGV", "http://localhost:8082/v1/status/");
    Agv agv2 = new Agv("Frontroom AGV", "http://localhost:8082/v1/status/");

    @Autowired
    private AgvService agvService;
    @Autowired
    private AgvLogEntryService agvLogEntryService;
    @Autowired
    private AgvLogEntryRepository agvLogEntryRepository;
    @Autowired
    private AgvObserverService agvObserverService;

    @BeforeEach
    void setUp() {
        agv1 = agvService.saveAgvToDatabase(agv1);
        agv2 = agvService.saveAgvToDatabase(agv2);
        agvService.notifyChange(agv1.getId());
        agvService.notifyChange(agv2.getId());
        agvObserverService.subscribe(agv1.getId(), agvLogEntryService);
    }

    @AfterEach
    void tearDown() {
        agvLogEntryRepository.deleteAll();
        agvService.getAgvMap().clear();
        agvObserverService.unsubscribe(agv1.getId(), agvLogEntryService);
    }

    @Test
    void subscribeToAgvObserverService() {
        assert agvObserverService.getSubscribersForAgv(agv1.getId()).contains(agvLogEntryService);
    }
}