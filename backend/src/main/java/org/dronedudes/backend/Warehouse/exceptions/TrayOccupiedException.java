package org.dronedudes.backend.Warehouse.exceptions;

public class TrayOccupiedException extends Throwable {
    public TrayOccupiedException(Long warehouseId, Long trayId) {
        super("Warehouse with ID: " + warehouseId + " already has an item in tray with ID: " + trayId);
    }
}
