package org.dronedudes.backend.Product;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dronedudes.backend.item.Item;

@Entity
@Table(name = "products")
@PrimaryKeyJoinColumn(name = "item_id")
@Getter
@Setter
@NoArgsConstructor
public class Product extends Item {
    private String description;

}
