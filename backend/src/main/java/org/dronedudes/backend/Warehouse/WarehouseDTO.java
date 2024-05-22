package org.dronedudes.backend.Warehouse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dronedudes.backend.common.Item;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseDTO {

    private WarehouseModel model;
    private int port;
    private String name;
    private Map<Long, Item> items;

}
