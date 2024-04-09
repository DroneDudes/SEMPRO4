package org.dronedudes.backend.Warehouse;

import org.springframework.stereotype.Component;
import org.dronedudes.backend.item.Item;
@Component
public class Warehouse {
    private int warehouseSize = 0;
    private Item[] warehouseItems;
    
    public void setWarehouseSize(int warehouseSize) {
        this.warehouseSize = warehouseSize;
    }
    public int getSize() {
        return warehouseSize;
    }
}
