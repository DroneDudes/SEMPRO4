package org.dronedudes.backend.Assembly;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.dronedudes.backend.Assembly.log.AssemblyLogEntry;
import org.dronedudes.backend.Assembly.log.AssemblyLogEntryService;
import org.dronedudes.backend.Assembly.sse.AssemblySseService;
import org.dronedudes.backend.common.ObserverService;
import org.dronedudes.backend.common.PublisherInterface;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Data
@Service
@Transactional
@RequiredArgsConstructor
public class AssemblyHandler implements PublisherInterface {

    private final AssemblyConnection assemblyConnection;
    private final AssemblyService assemblyService;
    private final AssemblyLogEntryService assemblyLogService;
    private final AssemblySseService assemblySseService;
    private final ObserverService observerService;

    int priorState = 0;
    int currentState = 0;
    int operationId;

    @PostConstruct
    public void logAssemblyStation(){
        AssemblyStation assemblyStation = new AssemblyStation();
        assemblyService.save(assemblyStation);
        assemblyService.getAssemblyMap().put(assemblyStation.getUuid(), assemblyStation);
        System.out.println(assemblyStation.getUuid());
        System.out.println(assemblyService.getAssemblyMap());
    }

    public void startProduction(int processId) {
        assemblyConnection.publish("emulator/operation", new Process(processId));
    }

    @PostConstruct
    public void subscribeToMqtt() {
        assemblyConnection.subscribeToStateAndCurrentOperation();
    }


//    @Scheduled(fixedDelay = 1000)
//    public void logAssemblyData() {
//        currentState = Integer.parseInt(assemblyConnection.getState());
//        operationId = Integer.parseInt(assemblyConnection.getCurrentOperation());
//
//        if (currentState != priorState) {
//            Optional<AssemblyStation> optionalAssemblyStation = assemblyService.getAssemblyStationById(1L);
//            AssemblyStation assemblyStation = optionalAssemblyStation.orElseThrow(() -> new RuntimeException("AssemblyStation not found"));
//            AssemblyLogEntry assemblyLogEntry = new AssemblyLogEntry(operationId, currentState, assemblyStation);
//            assemblyLogService.save(assemblyLogEntry);
//            assemblySseService.updateSse(assemblyLogEntry);
//            priorState = currentState;
//        }
//    }

    @Scheduled(fixedDelay = 1000)
    public void blabla(){
        System.out.println("YALLAH");
        for(AssemblyStation assemblyStation: assemblyService.getAssemblyMap().values()){
            currentState = Integer.parseInt(assemblyConnection.getState());
            operationId = Integer.parseInt(assemblyConnection.getCurrentOperation());
            System.out.println("YALLAH2222");
            if(assemblyStation.getState() != currentState || assemblyStation.getOperationId() != operationId){
                assemblyStation.setState(currentState);
                assemblyStation.setOperationId(operationId);
                assemblyService.getAssemblyMap().put(assemblyStation.getUuid(), assemblyStation);
                notifyChange(assemblyStation.getUuid());
                System.out.println("ALLLAHU AKKHBAAAAR");
            }


        }
    }

    @Override
    public void notifyChange(UUID machineId) {
        observerService.updateSubscribers(machineId);
    }
}

