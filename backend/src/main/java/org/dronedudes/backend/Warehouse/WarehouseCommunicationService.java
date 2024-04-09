package org.dronedudes.backend.Warehouse;

import jakarta.annotation.PostConstruct;
import org.dronedudes.backend.Warehouse.soap.SoapService;
import org.springframework.stereotype.Service;

@Service
public class WarehouseCommunicationService {
    private final SoapService soapService;

    public WarehouseCommunicationService(SoapService soapService) {
        this.soapService = soapService;
    }


    @PostConstruct
    public void testMethod() {
        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseCommunicationProtocol(WarehouseCommunicationProtocol.SOAP);

        pickItem(warehouse,3);
    }
    public void pickItem(Warehouse warehouse, int trayId) {
        if (warehouse.getWarehouseCommunicationProtocol() == WarehouseCommunicationProtocol.SOAP) {
            soapService.pickItem(warehouse, trayId);
        }
    }
}
