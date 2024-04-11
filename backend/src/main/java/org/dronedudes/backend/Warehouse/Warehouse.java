package org.dronedudes.backend.Warehouse;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dronedudes.backend.item.Item;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private WarehouseModel model;

    @ManyToMany
    @JoinTable(
            name = "warehouse_items",
            joinColumns = @JoinColumn(name = "warehouse_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private Set<Item> items;
    private String uri;
    private String name;

    public Warehouse(WarehouseModel model, int port, String name) {
        this.model = model;
        this.uri = model.getBaseUri() + port + model.getSuffixUri();
        this.name = name;
        this.items = new HashSet<>();
    }
}
