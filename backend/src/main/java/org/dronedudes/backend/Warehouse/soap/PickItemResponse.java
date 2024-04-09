package org.dronedudes.backend.Warehouse.soap;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PickItemResponse", propOrder = {
        "pickItemResult"
})
public class PickItemResponse {

    @XmlElement(name = "PickItemResult", nillable = true)
    protected String pickItemResult;

    public String getPickItemResult() {
        return pickItemResult;
    }

    public void setPickItemResult(String value) {
        this.pickItemResult = value;
    }

}
