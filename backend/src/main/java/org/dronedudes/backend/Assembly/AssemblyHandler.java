package org.dronedudes.backend.Assembly;

import jakarta.annotation.PostConstruct;
import org.dronedudes.backend.Assembly.log.AssemblyLog;
import org.dronedudes.backend.Assembly.log.AssemblyLogRepository;
import org.dronedudes.backend.Assembly.log.AssemblyLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AssemblyHandler {

    @Autowired
    private final AssemblyConnection assemblyConnection;

    @Autowired
    private final AssemblyService assemblyService;

    @Autowired
    private final AssemblyLogService assemblyLogService;


    int priorState = 0;
    int currentState = 0;
    int operationId;

    public AssemblyHandler(AssemblyConnection assemblyConnection, AssemblyService assemblyService, AssemblyLogService assemblyLogService) {
        this.assemblyConnection = assemblyConnection;
        this.assemblyService = assemblyService;
        this.assemblyLogService = assemblyLogService;

    }

    @PostConstruct
    public void logAssemblyStation(){
        AssemblyStation assemblyStation = new AssemblyStation();
        assemblyService.save(assemblyStation);
    }

    public void startProduction(int processId) {
        assemblyConnection.publish("emulator/operation", new Process(processId));
    }

    @PostConstruct
    public void subscribeToMqtt() {
        assemblyConnection.subscribeToStateAndCurrentOperation();
    }


    @Scheduled(fixedDelay = 1000)
    public void logAssemblyData() {
        currentState = Integer.parseInt(assemblyConnection.getState());
        operationId = Integer.parseInt(assemblyConnection.getCurrentOperation());

        if (currentState != priorState) {
            Optional<AssemblyStation> optionalAssemblyStation = assemblyService.getAssemblyStationById(1L);
            AssemblyStation assemblyStation = optionalAssemblyStation.orElseThrow(() -> new RuntimeException("AssemblyStation not found"));
            AssemblyLog assemblyLog = new AssemblyLog(operationId, currentState, assemblyStation);
            assemblyLogService.save(assemblyLog);
            priorState = currentState;
        }
    }
}

