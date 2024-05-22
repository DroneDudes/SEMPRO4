package org.dronedudes.backend.Warehouse.exceptions;

import org.dronedudes.backend.common.Item;

import java.util.Map;

public class NonEmptyWarehouseException extends Exception {

    private Map<Long, Item> items;
    public NonEmptyWarehouseException(Long warehouseId, Map<Long, Item> items) {
        super("Warehouse with ID " + warehouseId + " contains items and can't be deleted.");
        this.items = items;
    }
}
