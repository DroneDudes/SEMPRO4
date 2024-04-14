package org.dronedudes.backend.Warehouse.exceptions;

import org.dronedudes.backend.item.Item;

public class ItemNotFoundInWarehouse extends Exception {

    public ItemNotFoundInWarehouse(Item item, Long warehouseId) {
        super("Item: " + item + " was not found in Warehouse " + warehouseId);
    }
}
