package org.dronedudes.backend.Warehouse;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.dronedudes.backend.common.Item;

import java.util.HashMap;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private WarehouseModel model;

    @ManyToMany
    @JoinTable(
            name = "warehouse_item",
            joinColumns = @JoinColumn(name = "warehouse_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    @JsonSerialize(using = CustomItemMapSerializer.class)
    private Map<Long, Item> items;

    private String uri;
    private String name;
    private int size;


    public Warehouse(WarehouseModel model, int port, String name) {
        this.model = model;
        this.uri = model.getBaseUri() + port + model.getSuffixUri();
        this.name = name;
        this.items = new HashMap<>();
        this.size = model.getSize();
    }
}
