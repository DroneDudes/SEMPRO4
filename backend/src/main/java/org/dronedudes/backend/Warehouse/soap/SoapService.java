package org.dronedudes.backend.Warehouse.soap;


import org.dronedudes.backend.Warehouse.Warehouse;
import org.dronedudes.backend.common.Item;

import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;

@Service
public class SoapService {
    private final WebServiceTemplate webServiceTemplate;

    public SoapService(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    public String pickItem(Warehouse warehouse, int trayId) {
        PickItem pickItemRequest = new PickItem();
        pickItemRequest.setTrayId(trayId);
        PickItemResponse pickItemResponse = (PickItemResponse) webServiceTemplate.marshalSendAndReceive(
                warehouse.getUri(), pickItemRequest);
        return pickItemResponse.getPickItemResult();
    }


    public InsertItemResponse insertItem(Warehouse warehouse, int trayId, Item item) {
        InsertItem insertItemRequest = new InsertItem();
        insertItemRequest.setTrayId(trayId);
        insertItemRequest.setName(item.getId().toString());

        InsertItemResponse insertItemResponse = (InsertItemResponse)  webServiceTemplate.marshalSendAndReceive(
                warehouse.getUri(), insertItemRequest
        );
        return insertItemResponse;
    }

    public void getInventory(Warehouse warehouse) {
        GetInventory getInventoryRequest = new GetInventory();

        GetInventoryResponse getInventoryResponse = (GetInventoryResponse) webServiceTemplate.marshalSendAndReceive(
                warehouse.getUri()
        );

    }


}
