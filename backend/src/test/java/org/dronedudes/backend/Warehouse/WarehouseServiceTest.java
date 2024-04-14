package org.dronedudes.backend.Warehouse;

import jakarta.transaction.Transactional;
import org.dronedudes.backend.Part.Part;
import org.dronedudes.backend.Product.Product;
import org.dronedudes.backend.Warehouse.exceptions.ItemNotFoundInWarehouse;
import org.dronedudes.backend.Warehouse.exceptions.NonEmptyWarehouseException;
import org.dronedudes.backend.Warehouse.exceptions.WarehouseFullException;
import org.dronedudes.backend.Warehouse.exceptions.WarehouseNotFoundException;
import org.dronedudes.backend.Warehouse.soap.SoapService;
import org.dronedudes.backend.item.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class WarehouseServiceTest {

    @Mock
    private WarehouseRepository warehouseRepository;
    @Mock
    private SoapService soapService;
    @InjectMocks
    private WarehouseService warehouseService;

    @BeforeEach
    void setUp() {
        warehouseRepository = mock(WarehouseRepository.class);
        soapService = mock(SoapService.class);
        warehouseService = new WarehouseService(warehouseRepository, soapService);
    }

    @Test
    public void testCreateWarehouse(){
        WarehouseModel model = WarehouseModel.EFFIMAT10;
        int port = 8081;
        String name = "W01";
        warehouseService.createWarehouse(model, port, name);

        verify(warehouseRepository, times(1)).save(any(Warehouse.class));
    }
    @Test
    public void testGetAllWarehouses() {
        List<Warehouse> mockWarehouses = Arrays.asList(new Warehouse(), new Warehouse());
        when(warehouseRepository.findAll()).thenReturn(mockWarehouses);

        List<Warehouse> warehouses = warehouseService.getAllWarehouses();
        assertNotNull(warehouses);
        assertEquals(2, warehouses.size());
        verify(warehouseRepository, times(1)).findAll();
    }

    @Test
    public void testGetWarehouseFound() {
        Warehouse warehouse = new Warehouse();
        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));

        Optional<Warehouse> found = warehouseService.getWarehouse(1L);
        assertTrue(found.isPresent());
        assertEquals(warehouse, found.get());
    }

    @Test
    public void testGetWarehouseNotFound() {
        when(warehouseRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Warehouse> found = warehouseService.getWarehouse(1L);
        assertFalse(found.isPresent());
    }

    @Test
    public void testRemoveWarehouseSuccess() throws WarehouseNotFoundException, NonEmptyWarehouseException {
        Warehouse warehouse = new Warehouse();
        warehouse.setItems(new ArrayList<>());
        when(warehouseRepository.findById(anyLong())).thenReturn(Optional.of(warehouse));

        boolean result = warehouseService.removeWarehouse(1L);
        assertTrue(result);
        verify(warehouseRepository, times(1)).delete(warehouse);
    }

    @Test
    public void testRemoveWarehouseNotFound() {
        when(warehouseRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(WarehouseNotFoundException.class, () -> warehouseService.removeWarehouse(1L));
    }

    @Test
    public void testRemoveWarehouseNonEmpty() {
        Part part = new Part();
        Product product = new Product();

        WarehouseModel model = WarehouseModel.EFFIMAT10;
        Warehouse warehouse = new Warehouse(model, 8081, "W01");
        warehouse.setItems(new ArrayList<>(Arrays.asList(part, product)));

        when(warehouseRepository.findById(anyLong())).thenReturn(Optional.of(warehouse));

        assertThrows(NonEmptyWarehouseException.class, () -> warehouseService.removeWarehouse(1L));
    }

    @Test
    public void testAddItemToWarehouseSuccess() throws WarehouseFullException, WarehouseNotFoundException {
        WarehouseModel model = WarehouseModel.EFFIMAT10;
        Warehouse warehouse = new Warehouse(model, 8081, "W01");
        Item item = new Part();

        when(warehouseRepository.findById(anyLong())).thenReturn(Optional.of(warehouse));
        when(warehouseRepository.save(any(Warehouse.class))).thenReturn(warehouse);

        Warehouse updatedWarehouse = warehouseService.addItemToWarehouse(1L, item);

        assertTrue(updatedWarehouse.getItems().contains(item));
        verify(warehouseRepository).save(warehouse);
    }

    @Test
    public void testAddItemToWarehouseFull() {
        WarehouseModel model = WarehouseModel.EFFIMAT10;
        Warehouse warehouse = new Warehouse(model, 8081, "W01");

        System.out.println(model.getSize());
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < model.getSize(); i++) {
            Part part = new Part();
            part.setName("Part" + i);
            items.add(part);
        }
        warehouse.setItems(items);

        when(warehouseRepository.findById(anyLong())).thenReturn(Optional.of(warehouse));

        assertThrows(WarehouseFullException.class, () -> warehouseService.addItemToWarehouse(1L, new Part()));
    }


    @Test
    public void testAddItemToWarehouseNotFound() {
        when(warehouseRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(WarehouseNotFoundException.class, () -> warehouseService.addItemToWarehouse(1L, new Part()));
    }

    @Test
    public void testRemoveItemFromWarehouseSuccess() throws WarehouseNotFoundException, ItemNotFoundInWarehouse {
        Warehouse warehouse = new Warehouse();
        Item item = new Part();
        warehouse.setItems(new ArrayList<>(Collections.singleton(item)));

        when(warehouseRepository.findById(anyLong())).thenReturn(Optional.of(warehouse));
        when(warehouseRepository.save(any(Warehouse.class))).thenReturn(warehouse);

        Warehouse updatedWarehouse = warehouseService.removeItemFromWarehouse(1L, item);

        assertFalse(updatedWarehouse.getItems().contains(item));
        verify(warehouseRepository).save(warehouse);
    }

    @Test
    public void testRemoveItemFromWarehouseItemNotFound() throws WarehouseNotFoundException {
        Warehouse warehouse = new Warehouse();
        warehouse.setItems(new ArrayList<>());

        when(warehouseRepository.findById(anyLong())).thenReturn(Optional.of(warehouse));

        assertThrows(ItemNotFoundInWarehouse.class, () -> warehouseService.removeItemFromWarehouse(1L, new Part()));
    }

    @Test
    public void testRemoveItemFromWarehouseNotFound() {
        when(warehouseRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(WarehouseNotFoundException.class, () -> warehouseService.removeItemFromWarehouse(1L, new Part()));
    }
}
