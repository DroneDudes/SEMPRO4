package org.dronedudes.backend.Warehouse;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.dronedudes.backend.Warehouse.exceptions.*;
import org.dronedudes.backend.Warehouse.log.WarehouseLogEntry;
import org.dronedudes.backend.Warehouse.log.WarehouseLogEntryService;
import org.dronedudes.backend.Warehouse.soap.SoapService;
import org.dronedudes.backend.Warehouse.sse.SseWarehouseUpdateEvent;
import org.dronedudes.backend.common.IWarehouseService;
import org.dronedudes.backend.common.Item;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WarehouseService implements IWarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final SoapService soapService;
    private final WarehouseLogEntryService warehouseLogEntryService;

    private final ApplicationEventPublisher eventPublisher;
    private Map<Long, Warehouse> warehouses = new ConcurrentHashMap<>();


    @PostConstruct
    public void initializeBaseWarehouse(){

    }
    public WarehouseService(WarehouseRepository warehouseRepository,
                            SoapService soapService, ApplicationEventPublisher eventPublisher, WarehouseLogEntryService warehouseLogEntryService) {
        this.warehouseRepository = warehouseRepository;
        this.soapService = soapService;
        this.eventPublisher = eventPublisher;
        this.warehouseLogEntryService = warehouseLogEntryService;
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
        }
        eventPublisher.publishEvent(new SseWarehouseUpdateEvent(this, new ArrayList<>(warehouses.values())));
        return warehouse;
    }

    public UUID findWarehouseWithItem(Long itemId){
        for(Warehouse warehouse : warehouses.values()){
            for(Item item : warehouse.getItems().values()){
                if(item.getId().equals(itemId)){
                    return warehouse.getUuid();
                }
            }
        }
        return null;
    }

    public Optional<Warehouse> getWarehouseByUuid(UUID warehouseId) {
        return warehouses.values().stream()
                .filter(warehouse -> warehouse.getUuid().equals(warehouseId))
                .findFirst();
    }


    @Transactional
    public boolean removeWarehouse(Long warehouseId) throws WarehouseNotFoundException, NonEmptyWarehouseException {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(()-> new WarehouseNotFoundException(warehouseId));
        if(!warehouse.getItems().isEmpty()){
            throw new NonEmptyWarehouseException(warehouseId);
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
            throws WarehouseNotFoundException, WarehouseFullException, TrayOccupiedException {

        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new WarehouseNotFoundException(warehouseId));

        checkWarehouseCapacity(warehouse);

        // Check if the tray is already occupied
        if (warehouse.getItems().containsKey(trayId)) {
            throw new TrayOccupiedException(warehouseId, trayId);
        }

        warehouse.getItems().put(trayId, item);
        warehouseRepository.save(warehouse);
        warehouses.put(warehouse.getId(), warehouse);
        soapService.insertItem(warehouse, trayId.intValue(), item);
        eventPublisher.publishEvent(new SseWarehouseUpdateEvent(this, new ArrayList<>(warehouses.values())));
        warehouseLogEntryService.saveWarehouseLog(new WarehouseLogEntry(warehouses.get(warehouseId).getName(), item.getName() + " added to tray " + trayId));
        return warehouse;
    }

    @Transactional
    public Warehouse addItemToWarehouse(Long warehouseId, Item item)
            throws WarehouseNotFoundException, WarehouseFullException {


        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new WarehouseNotFoundException(warehouseId));

        Long trayId = findFirstAvailableSlot(warehouse);
        if (checkWarehouseCapacity(warehouse)) {
            warehouse.getItems().put(trayId, item);
        }

        warehouseRepository.save(warehouse);
        warehouses.put(warehouse.getId(), warehouse);
        eventPublisher.publishEvent(new SseWarehouseUpdateEvent(this, new ArrayList<>(warehouses.values())));
        warehouseLogEntryService.saveWarehouseLog(new WarehouseLogEntry(warehouses.get(warehouseId).getName(), item.getName() + " added to tray " + trayId));
        return warehouse;
    }

    @Transactional
    public Warehouse addItemToWarehouse(UUID warehouseUUID, Item item) throws WarehouseNotFoundException, WarehouseFullException {
        Optional<Warehouse> warehouseOptional = findWarehouseByUUID(warehouseUUID);
        if(warehouseOptional.isPresent()){
            return addItemToWarehouse(warehouseOptional.get().getId(), item);
        }
        return null;
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

    @Override
    public boolean pickItemFromWarehouse(UUID warehouseId, Long itemId) throws WarehouseNotFoundException, ItemNotFoundInWarehouse {
        Optional<Warehouse> warehouseOptional = getWarehouseByUuid(warehouseId);
        if(warehouseOptional.isEmpty()){
            throw new WarehouseNotFoundException(0L);
        }
        Long trayId = null;
        for(Map.Entry<Long, Item> entry : warehouseOptional.get().getItems().entrySet()){
            if(entry.getValue().getId().equals(itemId)){
                trayId = entry.getKey();
                break;
            }
        }
        if(trayId == null) {
            throw new ItemNotFoundInWarehouse(itemId, warehouseOptional.get().getId());
        }
        removeItemFromWarehouse(warehouseOptional.get().getId(), trayId);
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
        warehouseLogEntryService.saveWarehouseLog(new WarehouseLogEntry(warehouses.get(warehouseId).getName(),"Item removed from tray " + trayId));
        return warehouse;
    }

    public Warehouse setItems(Warehouse warehouse, Map<Long, Item> items) {
        warehouse.setItems(items);
        return warehouse;
    }

    public List<WarehouseModel> getWarehouseModels(){
        return List.of(WarehouseModel.EFFIMAT10);
    }

    @Override
    public List<UUID> getWarehousesWithEmptySpace() {
        List<UUID> warehousesWithEmptySpace = new ArrayList<>();
        warehouses.values().forEach(warehouse -> {
            try {
                findFirstAvailableSlot(warehouse);
                warehousesWithEmptySpace.add(warehouse.getUuid());
            } catch (WarehouseFullException ignored) {

            }
        });
        return warehousesWithEmptySpace;
    }

    public Optional<Warehouse> findWarehouseByUUID(UUID warehouseUuid) {
        return warehouses.values().stream()
                .filter(warehouse -> warehouse.getUuid().equals(warehouseUuid))
                .findFirst();
    }
}
