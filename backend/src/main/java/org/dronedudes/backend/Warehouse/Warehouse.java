package org.dronedudes.backend.Warehouse;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dronedudes.backend.item.Item;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private WarehouseModel model;


    @ManyToMany
    private Set<Item> items;
    private String uri;
    private String name;

    public Warehouse(WarehouseModel model, int port, String name) {
        this.model = model;
        this.uri = model.getBaseUri() + port + model.getSuffixUri();
        this.name = name;
    }
}
