package org.dronedudes.backend.Warehouse;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dronedudes.backend.item.Item;

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
    private List<Item> items;
    private String uri;
    private String name;

    public Warehouse(WarehouseModel model, int port, String name) {
        this.model = model;
        this.uri = model.getBaseUri() + port + model.getSuffixUri();
        this.name = name;
        this.items = new ArrayList<>();
    }
}
