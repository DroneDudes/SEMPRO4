package org.dronedudes.backend.Warehouse;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.dronedudes.backend.Warehouse.exceptions.ItemNotFoundInWarehouse;
import org.dronedudes.backend.Warehouse.exceptions.NonEmptyWarehouseException;
import org.dronedudes.backend.Warehouse.exceptions.WarehouseFullException;
import org.dronedudes.backend.Warehouse.exceptions.WarehouseNotFoundException;
import org.dronedudes.backend.Warehouse.soap.SoapService;
import org.dronedudes.backend.Warehouse.sse.SseWarehouseUpdateEvent;
import org.dronedudes.backend.item.Item;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WarehouseService{
    private final WarehouseRepository warehouseRepository;
    private final SoapService soapService;

    private final ApplicationEventPublisher eventPublisher;
    private Map<Long, Warehouse> warehouses = new ConcurrentHashMap<>();


    @PostConstruct
    public void initializeBaseWarehouse(){

    }
    public WarehouseService(WarehouseRepository warehouseRepository,
                            SoapService soapService, ApplicationEventPublisher eventPublisher) {
        this.warehouseRepository = warehouseRepository;
        this.soapService = soapService;
        this.eventPublisher = eventPublisher;
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
        try {
            for(int i = 1; i <= warehouse.getModel().getSize(); i++)
                soapService.pickItem(warehouse,i);
        } catch(Exception e){
            System.out.println("Could not remove");
        }
        eventPublisher.publishEvent(new SseWarehouseUpdateEvent(this, new ArrayList<>(warehouses.values())));
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
        warehouses.remove(warehouse.getId());
        eventPublisher.publishEvent(new SseWarehouseUpdateEvent(this, new ArrayList<>(warehouses.values())));
        return true;
    }

    @Transactional
    public List<Item> getWarehouseInventory(Long warehouseId) throws WarehouseNotFoundException {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new WarehouseNotFoundException(warehouseId));
        return new ArrayList<>(warehouse.getItems().values());
    }


    @Transactional
    public Warehouse addItemToWarehouse(Long warehouseId, Item item, Long trayId)
            throws WarehouseNotFoundException, WarehouseFullException {

        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new WarehouseNotFoundException(warehouseId));

        if (checkWarehouseCapacity(warehouse)) {
            warehouse.getItems().put(trayId, item);
        }
        warehouseRepository.save(warehouse);
        warehouses.put(warehouse.getId(), warehouse);
        soapService.insertItem(warehouse, trayId.intValue(), item);
        eventPublisher.publishEvent(new SseWarehouseUpdateEvent(this, new ArrayList<>(warehouses.values())));
        return warehouse;
    }

    @Transactional
    public Warehouse addItemToWarehouse(Long warehouseId, Item item)
            throws WarehouseNotFoundException, WarehouseFullException {

        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new WarehouseNotFoundException(warehouseId));

        //TODO fix check at pladsen er tom?!
        Long trayId = findFirstAvailableSlot(warehouse);
        if (checkWarehouseCapacity(warehouse)) {
            warehouse.getItems().put(trayId, item);
        }

        warehouseRepository.save(warehouse);
        warehouses.put(warehouse.getId(), warehouse);
        eventPublisher.publishEvent(new SseWarehouseUpdateEvent(this, new ArrayList<>(warehouses.values())));
        return warehouse;
    }

    private Long findFirstAvailableSlot(Warehouse warehouse) throws WarehouseFullException {
        int size = warehouse.getModel().getSize();
        Map<Long, Item> items = warehouse.getItems();
        for (long itemIndex = 1; itemIndex <= size; itemIndex++) {
            if (!items.containsKey(itemIndex)) {
                return itemIndex;
            }
        }
        throw new WarehouseFullException(warehouse.getId());
    }


    public boolean checkWarehouseCapacity(Warehouse warehouse)
            throws WarehouseFullException {

        if(warehouse.getItems().size() >= warehouse.getModel().getSize()) {
            throw new WarehouseFullException(warehouse.getId());
        }
        return true;
    }

    @Transactional
    public Warehouse removeItemFromWarehouse(Long warehouseId, Long trayId)
            throws WarehouseNotFoundException, ItemNotFoundInWarehouse {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new WarehouseNotFoundException(warehouseId));

        if (warehouse.getItems().remove(trayId) == null) {
            throw new ItemNotFoundInWarehouse(trayId, warehouseId);
        }
        warehouseRepository.save(warehouse);
        soapService.pickItem(warehouse, trayId.intValue());
        warehouses.put(warehouse.getId(), warehouse);
        eventPublisher.publishEvent(new SseWarehouseUpdateEvent(this, new ArrayList<>(warehouses.values())));
        return warehouse;
    }

    public Warehouse setItems(Warehouse warehouse, Map<Long, Item> items) {
        warehouse.setItems(items);
        return warehouse;
    }

    public List<WarehouseModel> getWarehouseModels(){
        return List.of(WarehouseModel.EFFIMAT10);
    }

}
