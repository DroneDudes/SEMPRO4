package org.dronedudes.backend.Warehouse;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.dronedudes.backend.item.Item;
@Entity
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private WarehouseModel model;
    private Item[] warehouseItems;
    private String uri = "http://localhost:8081/Service.asmx";
    private WarehouseCommunicationProtocol warehouseCommunicationProtocol;
    public void setWarehouseSize(int warehouseSize) {
        this.warehouseSize = warehouseSize;
    }
    public int getWarehouseSize() {
        return warehouseSize;
    }

    public Item[] getWarehouseItems() {
        return warehouseItems;
    }

    public void setWarehouseItems(Item[] warehouseItems) {
        this.warehouseItems = warehouseItems;
    }

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
