package org.dronedudes.backend.Product;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dronedudes.backend.common.Item;

@Entity
@Table(name = "products")
@PrimaryKeyJoinColumn(name = "item_id")
@Data
@NoArgsConstructor
public class Product extends Item {
    private String description;

}
