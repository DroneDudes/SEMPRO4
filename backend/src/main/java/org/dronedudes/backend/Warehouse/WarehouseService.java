package org.dronedudes.backend.Warehouse;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.dronedudes.backend.Warehouse.exceptions.NonEmptyWarehouseException;
import org.dronedudes.backend.Warehouse.exceptions.WarehouseNotFoundException;
import org.dronedudes.backend.Warehouse.soap.SoapService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WarehouseService{
    private Map<Long, Warehouse> warehouses;
    private final WarehouseRepository warehouseRepository;
    private final SoapService soapService;

    public WarehouseService(WarehouseRepository warehouseRepository,
                            SoapService soapService) {
        this.warehouses = new HashMap<>();
        this.warehouseRepository = warehouseRepository;
        this.soapService = soapService;
    }

    public List<Warehouse> getAllWarehouses () {
        return warehouseRepository.findAll();
    }
    public Optional<Warehouse> getWarehouse (Long id) {
        return warehouseRepository.findById(id);
    }
    @Transactional
    public Warehouse createWarehouse(WarehouseModel model, int port, String name) {
        Warehouse warehouse = new Warehouse(model, port, name);
        warehouseRepository.save(warehouse);
        warehouses.put(warehouse.getId(), warehouse);

        return warehouse;
    }

    @Transactional
    public boolean removeWarehouse(Long warehouseId) throws WarehouseNotFoundException, NonEmptyWarehouseException {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(()-> new WarehouseNotFoundException(warehouseId));
        if(!warehouse.getItems().isEmpty()){
            throw new NonEmptyWarehouseException(warehouseId, warehouse.getItems());
        }
        warehouseRepository.delete(warehouse);

        return true;
    }

}
