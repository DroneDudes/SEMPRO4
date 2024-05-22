package org.dronedudes.backend.common;

import org.dronedudes.backend.Warehouse.Warehouse;
import org.dronedudes.backend.Warehouse.exceptions.ItemNotFoundInWarehouse;
import org.dronedudes.backend.Warehouse.exceptions.WarehouseFullException;
import org.dronedudes.backend.Warehouse.exceptions.WarehouseNotFoundException;

import java.util.List;
import java.util.UUID;

public interface IWarehouseService {
    Warehouse addItemToWarehouse(Long warehouseId, Item item) throws WarehouseNotFoundException, WarehouseFullException;
    List<UUID> getWarehousesWithEmptySpace();
    Warehouse removeItemFromWarehouse(Long warehouseId, Long trayId) throws WarehouseNotFoundException, ItemNotFoundInWarehouse;
    UUID findWarehouseWithItem(Long itemId);
    boolean pickItemFromWarehouse(UUID warehouseId, Long itemId) throws WarehouseNotFoundException, ItemNotFoundInWarehouse;
}
