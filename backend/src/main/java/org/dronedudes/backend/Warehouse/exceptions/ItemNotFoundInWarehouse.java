package org.dronedudes.backend.Warehouse.exceptions;

import org.dronedudes.backend.item.Item;

public class ItemNotFoundInWarehouse extends Exception {

    public ItemNotFoundInWarehouse(Long trayId, Long warehouseId) {
        super("Item in tray : " + trayId + " was not found in Warehouse " + warehouseId);
    }
}
