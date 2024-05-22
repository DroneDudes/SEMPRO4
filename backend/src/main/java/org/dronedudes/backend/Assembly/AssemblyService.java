package org.dronedudes.backend.Assembly;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.dronedudes.backend.Blueprint.Blueprint;
import org.dronedudes.backend.Product.Product;
import org.dronedudes.backend.Product.ProductService;
import org.dronedudes.backend.common.IAssemblyService;
import org.dronedudes.backend.common.ObserverService;
import org.dronedudes.backend.common.PublisherInterface;
import org.dronedudes.backend.common.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.lang.Thread.sleep;

@Service
@Transactional
@Data
public class AssemblyService implements PublisherInterface, IAssemblyService {

    private Map<UUID, AssemblyStation> assemblyMap = new HashMap<>();
    private final AssemblyRepository assemblyRepository;
    private final AssemblyConnection assemblyConnection;
    private final ProductService productService;
    private final ObserverService observerService;
    private final Object assemblyLock = new Object();

    int priorState = 0;
    int currentState = 0;
    int operationId;

    @Autowired
    public AssemblyService(AssemblyRepository assemblyRepository, AssemblyConnection assemblyConnection, ProductService productService, ObserverService observerService) {
        this.assemblyRepository = assemblyRepository;
        this.assemblyConnection = assemblyConnection;
        this.productService = productService;
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
            if(assemblyStation.getState() != currentState || assemblyStation.getProcessId() != operationId){
                assemblyStation.setState(currentState);
                assemblyStation.setProcessId(operationId);
                notifyChange(assemblyStation.getUuid());
                System.out.println("Change registered in observer");
            }
        }
    }

    @Override
    public void notifyChange(UUID machineId) {
        observerService.updateSubscribers(machineId);
    }

    @Override
    public UUID getAvailableAssemblyId() {
        for (Map.Entry<UUID, AssemblyStation> entry : assemblyMap.entrySet()) {
            if (entry.getValue().getState() == 0) {
                return entry.getValue().getUuid();
            }
        }
        return null;
    }

    @Override
    public List<UUID> getAllAssemblyStationUuids() {
        List<UUID> allAssemblyUuids = new ArrayList<>();
        for(AssemblyStation assemblyStation: assemblyRepository.findAll()){
            allAssemblyUuids.add(assemblyStation.getUuid());
        }
        return allAssemblyUuids;
    }

    @Override
    public boolean assembleItem(UUID availableAssemblyStationUuid, Blueprint blueprint) {
        //if(availableAssemblyStationUuid != null){
            AssemblyStation assemblyStation = assemblyMap.get(availableAssemblyStationUuid);
            int processId = assemblyStation.getProcessId();
            assemblyStation.setBlueprintName("Assembling blueprint " + blueprint.getProductTitle());
            Product product = productService.saveProduct(blueprint.getProductTitle(), blueprint.getDescription());
            assemblyStation.setProduct(product);
            startProduction(processId + 1);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if(checkIfProcessing()) {
                System.out.println("Assembly stopped");
                return true;
            }
        //}
        return false;
    }

    @Override
    public Item getFinishedProductInAssemblyStation(UUID assemblyStationId) {
        return assemblyMap.get(assemblyStationId).getProduct();
    }

    public boolean checkIfProcessing(){
        while(Integer.parseInt(assemblyConnection.getState()) != 0){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } return true;
    }

    public void startProduction(int processId) {
        System.out.println("Assembly started");
        assemblyConnection.publish("emulator/operation", new Process(processId));
    }

//    public void save(AssemblyStation assemblyStation) {
//        assemblyRepository.save(assemblyStation);
//    }
}
