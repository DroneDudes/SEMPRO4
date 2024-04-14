package org.dronedudes.backend.Warehouse.soap;

import jakarta.annotation.PostConstruct;
import org.dronedudes.backend.Warehouse.Warehouse;
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

}
