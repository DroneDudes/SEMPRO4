package org.dronedudes.backend.Warehouse;

import org.springframework.stereotype.Component;

@Component
public class Warehouse {
    private int warehouseSize = 0;

    public void setWarehouseSize(int warehouseSize) {
        this.warehouseSize = warehouseSize;
    }
    public int getSize() {
        return warehouseSize;
    }
}
