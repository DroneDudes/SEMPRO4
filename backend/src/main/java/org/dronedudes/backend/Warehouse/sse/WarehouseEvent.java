package org.dronedudes.backend.Warehouse.sse;

import lombok.Getter;
import org.dronedudes.backend.Warehouse.Warehouse;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class WarehouseEvent extends ApplicationEvent {

    private final List<Warehouse> warehouses;
    public WarehouseEvent(Object source, List<Warehouse> warehouses) {
        super(source);
        this.warehouses = warehouses;
    }
}

