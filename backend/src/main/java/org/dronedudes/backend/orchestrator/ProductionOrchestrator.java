package org.dronedudes.backend.orchestrator;

import jakarta.annotation.PostConstruct;
import org.dronedudes.backend.Blueprint.Blueprint;
import org.dronedudes.backend.Part.Part;
import org.dronedudes.backend.Warehouse.exceptions.ItemNotFoundInWarehouse;
import org.dronedudes.backend.Warehouse.exceptions.WarehouseFullException;
import org.dronedudes.backend.Warehouse.exceptions.WarehouseNotFoundException;
import org.dronedudes.backend.common.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductionOrchestrator implements IMachineOrchestrator {

    private final ApplicationContext applicationContext;
    private final IAgvService agvService;
    private final IAssemblyService assemblyStationService;
    private final IWarehouseService warehouseService;
    private final ObserverService observerService;

    private final Map<UUID, ProductionState> productionStates = new HashMap<>();

    public ProductionOrchestrator(ApplicationContext applicationContext,
                                  IAgvService agvService,
                                  IWarehouseService warehouseService,
                                  ObserverService observerService,
                                  IAssemblyService assemblyStationService) {
        this.applicationContext = applicationContext;
        this.agvService = agvService;
        this.warehouseService = warehouseService;
        this.observerService = observerService;
        this.assemblyStationService = assemblyStationService;
    }

    @PostConstruct
    private void init() {
        //IAgvService = applicationContext.getBeansOfType(IAgvService.class).values().stream().toList().get(0); // get(0) da vi kun har én service klasse per machine
        //warehouseService = applicationContext.getBeansOfType(IWarehouseService.class).values().stream().toList().get(0);
    }

    @Override
    public void startProduction(int amount, IBlueprint iBlueprint) {
        // creating a new production state with a new uuuid
        UUID productionId = UUID.randomUUID();
        System.out.println(productionId);
        ProductionState productionState = new ProductionState(productionId);
        // saving it to the map
        productionStates.put(productionId, productionState);

        for(int i = 0; i < amount; i++) {
            //get available assembly station
            UUID availableAssemblyStationUuid = assemblyStationService.getAvailableAssemblyId();

            for(IPart part : iBlueprint.getIParts()){
                //find warehouse that has the part
                UUID warehouseUuidForWarehouseWithItem = warehouseService.findWarehouseWithItem(part.getId());
                if(warehouseUuidForWarehouseWithItem == null){
                    System.out.println("No warehouse contains the item");
                    return;
                }
                //find available agv
                UUID availableAgvUuid = agvService.getAvailableAgv();
                //move available agv to warehouse with item
                agvService.agvMoveToWarehouse(availableAgvUuid, warehouseUuidForWarehouseWithItem);

                //pick item from warehouse to tray
                try {
                    warehouseService.pickItemFromWarehouse(warehouseUuidForWarehouseWithItem, part.getId());
                } catch (WarehouseNotFoundException | ItemNotFoundInWarehouse e) {
                    throw new RuntimeException(e);
                }
                //pick item to agv
                agvService.agvPickUpItemFromWarehouse(availableAgvUuid, warehouseUuidForWarehouseWithItem, (Item) part);
                productionState.addPartOnAgv(part.getId(), availableAgvUuid);

                //move agv to assembly station
                agvService.agvMoveToAssemblyStation(availableAgvUuid, availableAssemblyStationUuid);
                //drop item at assembly station
                agvService.agvPutItemOnAssemblyStation(availableAgvUuid, availableAssemblyStationUuid);
                productionState.removePartFromAgv(part.getId());
                productionState.addPartOnAssemblyStation(part.getId(), availableAssemblyStationUuid);
            }

            Blueprint blueprint = new Blueprint();
            blueprint.setProductTitle(iBlueprint.getProductTitle());
            blueprint.setDescription(iBlueprint.getDescription());
            blueprint.setParts(iBlueprint.getIParts().stream().map(iPart ->
                    new Part(iPart.getName(),iPart.getDescription(), iPart.getSupplierDetails(), iPart.getPrice(), iPart.getBlueprints())
            ).collect(Collectors.toSet()));
            //assemble item
            assemblyStationService.assembleItem(availableAssemblyStationUuid, blueprint);
            productionState.removePartFromAssemblyStation(iBlueprint.getIParts().stream().toList().get(0).getId());

            UUID availableAgvUuid = agvService.getAvailableAgv();
            //move agv to assembly station
            agvService.agvMoveToAssemblyStation(availableAgvUuid, availableAssemblyStationUuid);
            Item item = assemblyStationService.getFinishedProductInAssemblyStation(availableAssemblyStationUuid);
            //pick item from assembly station
            agvService.agvPickUpItemFromAssemblyStation(availableAgvUuid, availableAssemblyStationUuid, item);
            productionState.addPartOnAgv(iBlueprint.getIParts().stream().toList().get(0).getId(), availableAgvUuid);

            // move item to warehouse
            UUID emptyWarehouseUuid = warehouseService.getWarehousesWithEmptySpace().get(0);
            agvService.agvMoveToWarehouse(availableAgvUuid, emptyWarehouseUuid);
            agvService.agvPutItemIntoWarehouse(availableAgvUuid, emptyWarehouseUuid);
            try {
                warehouseService.addItemToWarehouse(emptyWarehouseUuid, item);
            } catch (WarehouseNotFoundException e) {
                throw new RuntimeException(e);
            } catch (WarehouseFullException e) {
                throw new RuntimeException(e);
            }
            productionState.removePartFromAgv(iBlueprint.getIParts().stream().toList().get(0).getId());
        }

        productionStates.remove(productionId);
    }

    @Override
    public void stopProduction(UUID productionId) {
        ProductionState productionState = productionStates.get(productionId);
        if (productionState == null) {
            System.out.println("Production not found");
            return;
        }

        // empties the agv
        for (Map.Entry<Long, UUID> entry : productionState.getPartsOnAgv().entrySet()) {
            // get the agv uuid that carries a part tied to this production
            UUID agvId = entry.getValue();

            UUID emptyWarehouseUuid = warehouseService.getWarehousesWithEmptySpace().get(0);
            agvService.agvMoveToWarehouse(agvId, emptyWarehouseUuid);
            agvService.agvPutItemIntoWarehouse(agvId, emptyWarehouseUuid);
        }

        // empties the assembly station
        for (Map.Entry<Long, UUID> entry : productionState.getPartsOnAssemblyStation().entrySet()) {
            // get the assembly station uuid that has part(s) for this production
            UUID assemblyStationId = entry.getValue();

            UUID availableAgvUuid = agvService.getAvailableAgv();
            agvService.agvMoveToAssemblyStation(availableAgvUuid, assemblyStationId);

            Item item = assemblyStationService.getFinishedProductInAssemblyStation(assemblyStationId);
            agvService.agvPickUpItemFromAssemblyStation(availableAgvUuid, assemblyStationId, item);

            UUID emptyWarehouseUuid = warehouseService.getWarehousesWithEmptySpace().get(0);
            agvService.agvMoveToWarehouse(availableAgvUuid, emptyWarehouseUuid);
            agvService.agvPutItemIntoWarehouse(availableAgvUuid, emptyWarehouseUuid);
        }

        productionStates.remove(productionId);
    }
}
