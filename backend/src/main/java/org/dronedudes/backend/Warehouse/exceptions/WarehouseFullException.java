package org.dronedudes.backend.Warehouse.exceptions;

public class WarehouseFullException extends Exception {
    public WarehouseFullException(Long warehouseId) {
        super("Warehouse with ID: " + warehouseId + " is full");
    }
}
