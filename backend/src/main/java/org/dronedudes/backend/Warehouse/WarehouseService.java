package org.dronedudes.backend.Warehouse;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.dronedudes.backend.Warehouse.exceptions.ItemNotFoundInWarehouse;
import org.dronedudes.backend.Warehouse.exceptions.NonEmptyWarehouseException;
import org.dronedudes.backend.Warehouse.exceptions.WarehouseFullException;
import org.dronedudes.backend.Warehouse.exceptions.WarehouseNotFoundException;
import org.dronedudes.backend.Warehouse.soap.SoapService;
import org.dronedudes.backend.item.Item;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WarehouseService{
    private final WarehouseRepository warehouseRepository;
    private final SoapService soapService;

    @PostConstruct
    public void initializeBaseWarehouse(){

    }
    public WarehouseService(WarehouseRepository warehouseRepository,
                            SoapService soapService) {
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

        return warehouse;
    }

    @Transactional
    public boolean removeWarehouse(Long warehouseId)
            throws WarehouseNotFoundException, NonEmptyWarehouseException {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(()-> new WarehouseNotFoundException(warehouseId));
        if(!warehouse.getItems().isEmpty()){
            throw new NonEmptyWarehouseException(warehouseId, warehouse.getItems());
        }
        warehouseRepository.delete(warehouse);
        return true;
    }

    @Transactional
    public Warehouse addItemToWarehouse(Long warehouseId, Item item)
            throws WarehouseNotFoundException, WarehouseFullException {

        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new WarehouseNotFoundException(warehouseId));

        if(checkWarehouseCapacity(warehouse)){
            warehouse.getItems().add(item);
        }
        return warehouseRepository.save(warehouse);
    }

    public boolean checkWarehouseCapacity(Warehouse warehouse)
            throws WarehouseFullException {

        if(warehouse.getItems().size() >= warehouse.getModel().getSize()) {
            throw new WarehouseFullException(warehouse.getId());
        }
        return true;
    }

    @Transactional
    public Warehouse removeItemFromWarehouse(Long warehouseId, Item item)
            throws WarehouseNotFoundException, ItemNotFoundInWarehouse{
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new WarehouseNotFoundException(warehouseId));

        boolean itemRemoved = warehouse.getItems().removeIf(i -> i.equals(item));
        if(!itemRemoved) {
            throw new ItemNotFoundInWarehouse(item, warehouseId);
        }

        return warehouseRepository.save(warehouse);
    }


}
