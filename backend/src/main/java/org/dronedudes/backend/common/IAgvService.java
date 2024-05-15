package org.dronedudes.backend.common;

import java.util.UUID;

public interface IAgvService {
    public boolean agvMoveToAssemblyStation(UUID agvId, UUID destinationMachineId);
    public boolean agvMoveToWarehouse(UUID agvMachineId, UUID destinationMachineId);
    public boolean agvPickUpItemFromAssemblyStation(UUID agvMachineId, UUID assemblyStationMachineId, Item item);
    public boolean agvPutItemOnAssemblyStation(UUID agvMachineId,UUID assemblyStationMachineId);
    public boolean agvPickUpItemFromWarehouse(UUID agvMachineId, UUID warehouseMachineId, Item item);
    public boolean agvPutItemIntoWarehouse(UUID agvMachineId, UUID warehouseMachineId);
    public boolean agvMoveToChargingStation(UUID agvMachineId, UUID chargerId);
    public UUID getAvailableAgv();
    public void test();
}
