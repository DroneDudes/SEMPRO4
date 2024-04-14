package org.dronedudes.backend.Warehouse.exceptions;

public class WarehouseNotFoundException extends RuntimeException {

    public WarehouseNotFoundException(Long warehouseId) {
        super("Warehouse not found with ID: " + warehouseId);
    }
}
