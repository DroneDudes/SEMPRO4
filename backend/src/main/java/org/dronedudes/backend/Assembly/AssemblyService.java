package org.dronedudes.backend.Assembly;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dronedudes.backend.Assembly.log.AssemblyLogEntryService;
import org.dronedudes.backend.Assembly.sse.AssemblySseService;
import org.dronedudes.backend.common.ObserverService;
import org.dronedudes.backend.common.PublisherInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.lang.Thread.sleep;

@Service
@Transactional
@Data
public class AssemblyService implements PublisherInterface {

    private Map<UUID, AssemblyStation> assemblyMap = new HashMap<>();
    private final AssemblyRepository assemblyRepository;
    private final AssemblyConnection assemblyConnection;
    private final ObserverService observerService;

    int priorState = 0;
    int currentState = 0;
    int operationId;

    @Autowired
    public AssemblyService(AssemblyRepository assemblyRepository, AssemblyConnection assemblyConnection, ObserverService observerService) {
        this.assemblyRepository = assemblyRepository;
        this.assemblyConnection = assemblyConnection;
        this.observerService = observerService;
        this.assemblyConnection.subscribeToStateAndCurrentOperation();
    }

//    @PostConstruct
//    public void subscribeToMqtt() {
//        assemblyConnection.subscribeToStateAndCurrentOperation();
//    }

    @PostConstruct
    public void saveAssemblyStationOnStart(){
        AssemblyStation assemblyStation = new AssemblyStation();
        assemblyMap.put(assemblyStation.getUuid(), assemblyStation);
        notifyChange(assemblyStation.getUuid());
        assemblyRepository.save(assemblyStation);
    }

    public Optional<AssemblyStation> getAssemblyStation() {
        return assemblyRepository.findFirstByOrderById();
    }

    @Scheduled(fixedDelay = 1000)
    public void pollAllAssemblyStations(){
        for(AssemblyStation assemblyStation: assemblyMap.values()){
            currentState = Integer.parseInt(assemblyConnection.getState());
            operationId = Integer.parseInt(assemblyConnection.getCurrentOperation());
            if(assemblyStation.getState() != currentState || assemblyStation.getOperationId() != operationId){
                assemblyStation.setState(currentState);
                assemblyStation.setOperationId(operationId);
                notifyChange(assemblyStation.getUuid());
                System.out.println("ALLLAHU AKKHBAAAAR");
            }
        }
    }

    @Override
    public void notifyChange(UUID machineId) {
        observerService.updateSubscribers(machineId);
    }

//    public void startProduction(int processId) {
//        assemblyConnection.publish("emulator/operation", new Process(processId));
//    }

//    public void save(AssemblyStation assemblyStation) {
//        assemblyRepository.save(assemblyStation);
//    }
}
