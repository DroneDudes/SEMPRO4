package org.dronedudes.backend.Warehouse;

import org.dronedudes.backend.Warehouse.soap.SoapService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class WarehouseService {
    private List<Warehouse> warehouses;
    private final WarehouseCommunicationService warehouseCommunicationService;

    public WarehouseService(WarehouseCommunicationService warehouseCommunicationService) {
        this.warehouseCommunicationService = warehouseCommunicationService;
    }

}
