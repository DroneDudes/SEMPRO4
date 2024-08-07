package org.dronedudes.backend.Warehouse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WarehouseModel {
    EFFIMAT10(10, "http://localhost:", "/Service.asmx");
    private final int size;
    private final String baseUri;
    private final String suffixUri;
}
