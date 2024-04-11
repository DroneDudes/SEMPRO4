package org.dronedudes.backend.Warehouse;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service

public class WarehouseService{
    private Map<Long, Warehouse> warehouses;
    private final WarehouseRepository warehouseRepository;

    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public List<Warehouse> getAllWarehouses () {
        return warehouseRepository.findAll();
    }
    public Optional<Warehouse> getWarehouse (Long id) {
        return warehouseRepository.findById(id);
    }
    @PostConstruct
    public void test(){

        Warehouse warehouse = createWarehouse(WarehouseModel.EFFIMAT10, 8081, "W01");
        System.out.println(warehouse.getId());
    }
    public Warehouse createWarehouse(WarehouseModel model, int port, String name) {
        Warehouse warehouse = new Warehouse(model, port, name);
        warehouseRepository.save(warehouse);
        return warehouse;
    }
}
