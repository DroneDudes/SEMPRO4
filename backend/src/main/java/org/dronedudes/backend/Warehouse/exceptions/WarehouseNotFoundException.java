package org.dronedudes.backend.Warehouse.exceptions;

public class WarehouseNotFoundException extends Exception {

    public WarehouseNotFoundException(Long warehouseId) {
        super("Warehouse not found with ID: " + warehouseId);
    }
}
