package org.dronedudes.backend.orchestrator;

import jakarta.annotation.PostConstruct;
import org.dronedudes.backend.Warehouse.exceptions.ItemNotFoundInWarehouse;
import org.dronedudes.backend.Warehouse.exceptions.WarehouseNotFoundException;
import org.dronedudes.backend.common.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductionOrchestrator implements IMachineOrchestrator, ApplicationListener<ApplicationReadyEvent> {

    private final ApplicationContext applicationContext;
    private final IAgvService agvService;
    private IAssemblyService assemblyStationService;
    private final IWarehouseService warehouseService;
    private final ObserverService observerService;


    public ProductionOrchestrator(ApplicationContext applicationContext,
                                  IAgvService agvService,
                                  IWarehouseService warehouseService,
                                  ObserverService observerService/*,
                                  IAssemblyService assemblyStationService*/) {
        this.applicationContext = applicationContext;
        this.agvService = agvService;
        this.warehouseService = warehouseService;
        this.observerService = observerService;
        //this.assemblyStationService = assemblyStationService;
    }

    @PostConstruct
    private void init() {
        //IAgvService = applicationContext.getBeansOfType(IAgvService.class).values().stream().toList().get(0); // get(0) da vi kun har én service klasse per machine
        //warehouseService = applicationContext.getBeansOfType(IWarehouseService.class).values().stream().toList().get(0);
    }

    @Override
    public void startProduction(int amount, IBlueprint IBlueprint) {
        for(int i = 0; i < amount; i++) {
            for(IPart part : IBlueprint.getParts()){
                //find warehouse that has the part
                UUID warehouseUuidForWarehouseWithItem = warehouseService.findWarehouseWithItem(part.getId());
                if(warehouseUuidForWarehouseWithItem == null){
                    System.out.println("No warehouse contains the item");
                    return;
                }
                //find available agv
                UUID availableAgvUuid = agvService.getAvailableAgv();
                //move avaible agv to warehouse with item
                agvService.agvMoveToWarehouse(availableAgvUuid, warehouseUuidForWarehouseWithItem);

                //pick item from warehouse to tray
                try {
                    warehouseService.pickItemFromWarehouse(warehouseUuidForWarehouseWithItem, part.getId());
                } catch (WarehouseNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (ItemNotFoundInWarehouse e) {
                    throw new RuntimeException(e);
                }
                //pick item to agv
                agvService.agvPickUpItemFromWarehouse(availableAgvUuid, warehouseUuidForWarehouseWithItem, (Item) part);

                //get available assembly station
                //UUID availableAssemblyStationUuid = assemblyStationService.getAvailableAssemblyId();
                //move agv to assembly station
                //agvService.agvMoveToAssemblyStation(availableAgvUuid,availableAssemblyStationUuid);
                //drop item at assembly station
                //agvService.agvPutItemOnAssemblyStation(availableAgvUuid, availableAssemblyStationUuid);
                //assemble item
                //assemblyStationService.assembleItem(availableAssemblyStationUuid, (Item) part);
                //pick item from assembly station
                //agvService.agvPickUpItemFromAssemblyStation(availableAgvUuid, availableAssemblyStationUuid, (Item) part);
                //move agv to warehouse
                //agvService.agvMoveToWarehouse(availableAgvUuid, warehouseUuidForWarehouseWithItem);
                //put item in warehouse
                //agvService.agvPutItemIntoWarehouse(availableAgvUuid, warehouseUuidForWarehouseWithItem);
            }
        }
    }

    @Override
    public void stopProduction() {

    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println(warehouseService.getWarehousesWithEmptySpace());
    }





}
