package org.dronedudes.backend.common;

import org.dronedudes.backend.agv.state.AgvStateEnum;

import java.util.UUID;

public interface IAgvService {
    boolean agvMoveToAssemblyStation(UUID agvId, UUID destinationMachineId);
    boolean agvMoveToWarehouse(UUID agvMachineId, UUID destinationMachineId);
    boolean agvPickUpItemFromAssemblyStation(UUID agvMachineId, UUID assemblyStationMachineId, Item item);
    boolean agvPutItemOnAssemblyStation(UUID agvMachineId,UUID assemblyStationMachineId);
    boolean agvPickUpItemFromWarehouse(UUID agvMachineId, UUID warehouseMachineId, Item item);
    boolean agvPutItemIntoWarehouse(UUID agvMachineId, UUID warehouseMachineId);
    boolean agvMoveToChargingStation(UUID agvMachineId);
    UUID getAvailableAgv();
    AgvStateEnum getAgvState(UUID agvMachineId);

}
