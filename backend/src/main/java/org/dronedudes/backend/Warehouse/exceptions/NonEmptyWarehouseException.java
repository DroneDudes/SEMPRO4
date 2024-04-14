package org.dronedudes.backend.Warehouse.exceptions;

import org.dronedudes.backend.item.Item;

import java.util.Set;

public class NonEmptyWarehouseException extends RuntimeException {

    private Set<Item> items;
    public NonEmptyWarehouseException(Long warehouseId, Set<Item> items) {
        super("Warehouse with ID " + warehouseId + " contains items and can't be deleted.");
        this.items = items;
    }
}
