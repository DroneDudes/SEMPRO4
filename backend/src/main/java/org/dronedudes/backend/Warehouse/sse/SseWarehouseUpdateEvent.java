package org.dronedudes.backend.Warehouse.sse;

import org.dronedudes.backend.Warehouse.Warehouse;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class SseWarehouseUpdateEvent extends ApplicationEvent {
    private final List<Warehouse> warehouses;

    public SseWarehouseUpdateEvent(Object source, List<Warehouse> warehouses) {
        super(source);
        this.warehouses = warehouses;
    }

    public List<Warehouse> getWarehouses() {
        return warehouses;
    }
}
