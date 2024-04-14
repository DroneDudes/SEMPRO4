package org.dronedudes.backend.Warehouse.exceptions;

import org.dronedudes.backend.item.Item;

import java.util.List;

public class NonEmptyWarehouseException extends Exception {

    private List<Item> items;
    public NonEmptyWarehouseException(Long warehouseId, List<Item> items) {
        super("Warehouse with ID " + warehouseId + " contains items and can't be deleted.");
        this.items = items;
    }
}
