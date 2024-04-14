package org.dronedudes.backend.Warehouse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseDTO {

    private WarehouseModel model;
    private int port;
    private String name;

}
