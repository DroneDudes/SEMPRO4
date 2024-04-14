package org.dronedudes.backend.Warehouse;

import org.springframework.beans.factory.annotation.Value;
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

    public WarehouseConfiguration(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @Bean
    public Warehouse createStandardWarehouse(){
        return warehouseService.createWarehouse(model, port, name);
    }
}
