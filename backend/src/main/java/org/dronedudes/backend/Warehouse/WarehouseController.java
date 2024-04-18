package org.dronedudes.backend.Warehouse;

import org.dronedudes.backend.Warehouse.exceptions.ItemNotFoundInWarehouse;
import org.dronedudes.backend.Warehouse.exceptions.NonEmptyWarehouseException;
import org.dronedudes.backend.Warehouse.exceptions.WarehouseFullException;
import org.dronedudes.backend.Warehouse.exceptions.WarehouseNotFoundException;
import org.dronedudes.backend.item.Item;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PostMapping
    public ResponseEntity<Warehouse> createWarehouse(@RequestBody WarehouseDTO warehouseDTO) {
        Warehouse warehouse = warehouseService.createWarehouse(
                warehouseDTO.getModel(),
                warehouseDTO.getPort(),
                warehouseDTO.getName()
        );
        return ResponseEntity.ok(warehouse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Warehouse> getWarehouse(@PathVariable Long id) {
        return warehouseService.getWarehouse(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Warehouse>> getAllWarehouses() {
        List<Warehouse> warehouses = warehouseService.getAllWarehouses();
        return ResponseEntity.ok(warehouses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeWarehouse(@PathVariable Long id) {
        try {
            warehouseService.removeWarehouse(id);
            return ResponseEntity.ok().build();
        } catch (WarehouseNotFoundException e){
            return ResponseEntity.notFound().build();
        } catch (NonEmptyWarehouseException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<List<Item>> getWarehouseInventory(@PathVariable Long id){
        try{
            List<Item> items = warehouseService.getWarehouseInventory(id);
            return ResponseEntity.ok(items);
        } catch (WarehouseNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<Warehouse> addItemToWarehouse(@PathVariable Long id, @RequestBody Item item) {
        try {
            Warehouse warehouse = warehouseService.addItemToWarehouse(id, item);
            return ResponseEntity.ok(warehouse);
        } catch (WarehouseNotFoundException warehouseNotFoundException){
            return ResponseEntity.notFound().build();
        } catch (WarehouseFullException warehouseFullException) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("/{id}/items/{trayId}")
    public ResponseEntity<Warehouse> addItemToWarehouseWithTrayId(@PathVariable Long id,@PathVariable Long trayId, @RequestBody Item item) {
        try {
            Warehouse warehouse = warehouseService.addItemToWarehouse(id, item, trayId);
            return ResponseEntity.ok(warehouse);
        } catch (WarehouseNotFoundException warehouseNotFoundException){
            return ResponseEntity.notFound().build();
        } catch (WarehouseFullException warehouseFullException) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}/items/{trayId}")
    public ResponseEntity<Warehouse> removeItemFromWarehouse(@PathVariable Long id, @PathVariable Long trayId) {
        try {
            Warehouse warehouse = warehouseService.removeItemFromWarehouse(id, trayId);
            return ResponseEntity.ok(warehouse);
        } catch (WarehouseNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ItemNotFoundInWarehouse e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/models")
    public ResponseEntity<List<WarehouseModel>> getWarehouseModels(){
        return ResponseEntity.ok(warehouseService.getWarehouseModels());
    }

}
