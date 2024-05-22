package org.dronedudes.backend.common;


import org.dronedudes.backend.Blueprint.Blueprint;
import java.util.List;

public interface IPart {
    Long getId();
    String getName();
    String getDescription();
    String getSpecifications();
    String getSupplierDetails();
    long getPrice();
    List<Blueprint> getBlueprints();
}
