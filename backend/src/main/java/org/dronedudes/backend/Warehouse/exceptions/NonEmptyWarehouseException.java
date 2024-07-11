package org.dronedudes.backend.Warehouse.exceptions;

public class NonEmptyWarehouseException extends Exception {


    public NonEmptyWarehouseException(Long warehouseId) {
        super("Warehouse with ID " + warehouseId + " contains items and can't be deleted.");
    }
}
