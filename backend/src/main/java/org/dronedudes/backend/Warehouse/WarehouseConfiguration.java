package org.dronedudes.backend.Warehouse;

import org.dronedudes.backend.Warehouse.exceptions.WarehouseFullException;
import org.dronedudes.backend.Warehouse.exceptions.WarehouseNotFoundException;
import org.dronedudes.backend.common.WarehouseReadyEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WarehouseConfiguration {

    @Value("${warehouse.model}")
    private WarehouseModel model;
    @Value("${warehouse.port}")
    private int port;
    @Value("${warehouse.name}")
    private String name;

    private final WarehouseService warehouseService;
    private final ApplicationEventPublisher eventPublisher;

    public WarehouseConfiguration(WarehouseService warehouseService,
                                  ApplicationEventPublisher eventPublisher) {
        this.warehouseService = warehouseService;
        this.eventPublisher = eventPublisher;
    }

    @Bean
    public Warehouse createStandardWarehouse() throws WarehouseNotFoundException, WarehouseFullException {
        Warehouse warehouse =  warehouseService.createWarehouse(model, port, name);
        eventPublisher.publishEvent(new WarehouseReadyEvent(this));
        return warehouse;
    }
}
