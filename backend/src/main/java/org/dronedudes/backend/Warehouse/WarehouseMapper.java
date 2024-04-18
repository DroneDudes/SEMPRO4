package org.dronedudes.backend.Warehouse;

import org.springframework.stereotype.Component;

@Component
public class WarehouseMapper {

    public WarehouseDTO mapToDTO(Warehouse warehouse) {
        WarehouseDTO dto = new WarehouseDTO();
        dto.setModel(warehouse.getModel());
        dto.setName(warehouse.getName());
        dto.setPort(getPortFromUri(warehouse.getUri()));
        return dto;
    }

    public Warehouse mapToEntity(WarehouseDTO warehouseDTO) {
        Warehouse warehouse = new Warehouse();
        warehouse.setModel(warehouseDTO.getModel());
        warehouse.setName(warehouseDTO.getName());
        warehouse.setUri(generateUri(warehouseDTO.getModel(), warehouseDTO.getPort()));
        return warehouse;
    }

    private int getPortFromUri(String uri) {
        String[] parts = uri.split("/");
        String portString = parts[parts.length - 1];
        return Integer.parseInt(portString);
    }

    private String generateUri(WarehouseModel model, int port) {
        return model.getBaseUri() + port + model.getSuffixUri();
    }
}