package org.dronedudes.backend.Warehouse.soap;

import org.dronedudes.backend.Part.Part;
import org.dronedudes.backend.Warehouse.Warehouse;
import org.dronedudes.backend.item.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ws.client.core.WebServiceTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SoapServiceTest {

    @Mock
    private WebServiceTemplate webServiceTemplate;

    @InjectMocks
    private SoapService soapService;

    private Warehouse warehouse;
    private Item item;

    @BeforeEach
    void setUp() {
        warehouse = new Warehouse();
        warehouse.setUri("http://testurl:8081");

        item = new Part();
        item.setName("Test Part");
    }

    @Test
    void testPickItem() {
        int trayId = 1;
        PickItemResponse mockResponse = new PickItemResponse();
        mockResponse.setPickItemResult("Item Picked");

        when(webServiceTemplate.marshalSendAndReceive(anyString(), any(PickItem.class)))
                .thenReturn(mockResponse);

        String result = soapService.pickItem(warehouse, trayId);

        assertEquals("Item Picked", result);
        verify(webServiceTemplate).marshalSendAndReceive(eq(warehouse.getUri()), any(PickItem.class));
    }

    @Test
    void testInsertItem() {
        int trayId = 1;
        InsertItemResponse mockResponse = new InsertItemResponse();
        mockResponse.setInsertItemResult("Item Inserted");

        when(webServiceTemplate.marshalSendAndReceive(anyString(), any(InsertItem.class)))
                .thenReturn(mockResponse);

        InsertItemResponse result = soapService.insertItem(warehouse, trayId, item);

        assertEquals("Item Inserted", result.getInsertItemResult());
        verify(webServiceTemplate).marshalSendAndReceive(eq(warehouse.getUri()), any(InsertItem.class));
    }

    // TODO Create test for getInventory()
}

