package org.dronedudes.backend.Warehouse.sse;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.dronedudes.backend.Warehouse.Warehouse;

import java.util.List;

@Component
public class WarehouseEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public WarehouseEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishWarehouseUpdateEvent(List<Warehouse> warehouses) {
        WarehouseUpdateEvent event = new WarehouseUpdateEvent(this, warehouses);
        applicationEventPublisher.publishEvent(event);
    }
}

