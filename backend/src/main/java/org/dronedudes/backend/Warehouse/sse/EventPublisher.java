package org.dronedudes.backend.Warehouse.sse;

import org.dronedudes.backend.Warehouse.Warehouse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventPublisher {
    private final ApplicationEventPublisher publisher;

    public EventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publishWarehouseEvent(List<Warehouse> warehouse) {
        WarehouseEvent event = new WarehouseEvent(this, warehouse);
        publisher.publishEvent(event);
    }
}

