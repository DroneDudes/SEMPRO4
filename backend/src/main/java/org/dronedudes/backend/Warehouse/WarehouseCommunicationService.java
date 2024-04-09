package org.dronedudes.backend.Warehouse;

import org.dronedudes.backend.Warehouse.soap.SoapService;
import org.springframework.stereotype.Service;

@Service
public class WarehouseCommunicationService {
    private final SoapService soapService;

    public WarehouseCommunicationService(SoapService soapService) {
        this.soapService = soapService;
    }

    public void pickItem(Warehouse warehouse, int trayId) {
        if (warehouse.getWarehouseCommunicationProtocol() == WarehouseCommunicationProtocol.SOAP) {
            soapService.pickItem(warehouse, trayId);
        }
    }
}
