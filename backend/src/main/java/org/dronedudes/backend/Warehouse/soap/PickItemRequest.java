package org.dronedudes.backend.Warehouse.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PickItem", propOrder = {
        "trayId"
})
public class PickItemRequest {

    @XmlElement(required = true)
    protected int trayId;

    public int getTrayId() {
        return trayId;
    }

    public void setTrayId(int value) {
        this.trayId = value;
    }

}
