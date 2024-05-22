package org.dronedudes.backend.common;

import java.util.Set;

public interface IBlueprint {
    Long getId();
    String getProductTitle();
    String getDescription();
    Set<IPart> getIParts();
    void addPart(IPart IPart);
}
