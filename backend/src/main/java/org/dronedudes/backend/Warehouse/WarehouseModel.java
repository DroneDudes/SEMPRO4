package org.dronedudes.backend.Warehouse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WarehouseModel {
    EFFIMAT10(10, 8081);
    private final int size;
    private final int port;
}
