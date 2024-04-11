package org.dronedudes.backend.Warehouse;

import jakarta.persistence.*;
import org.dronedudes.backend.item.Item;
import org.hibernate.annotations.ManyToAny;

import java.util.Map;
import java.util.Set;

@Entity
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private WarehouseModel model;
    @ManyToAny
    private Set<Item> items;
    private String uri = "http://localhost:8081/Service.asmx";
    private WarehouseCommunicationProtocol warehouseCommunicationProtocol;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public WarehouseCommunicationProtocol getWarehouseCommunicationProtocol() {
        return warehouseCommunicationProtocol;
    }

    public void setWarehouseCommunicationProtocol(WarehouseCommunicationProtocol warehouseCommunicationProtocol) {
        this.warehouseCommunicationProtocol = warehouseCommunicationProtocol;
    }
}
