package org.dronedudes.backend.Warehouse;

import org.dronedudes.backend.Part.Part;
import org.dronedudes.backend.Product.Product;
import org.dronedudes.backend.Warehouse.exceptions.NonEmptyWarehouseException;
import org.dronedudes.backend.Warehouse.exceptions.WarehouseNotFoundException;
import org.dronedudes.backend.Warehouse.soap.SoapService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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
        warehouse.setItems(new HashSet<>());
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
        Warehouse warehouse = new Warehouse();
        warehouse.setItems(new HashSet<>(Arrays.asList(new Part(), new Product())));
        when(warehouseRepository.findById(anyLong())).thenReturn(Optional.of(warehouse));

        assertThrows(NonEmptyWarehouseException.class, () -> warehouseService.removeWarehouse(1L));
    }
}
