package org.dronedudes.backend.Blueprint;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dronedudes.backend.Part.Part;

import java.util.ArrayList;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductBlueprint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String productTitle;
    private String description;
    //private ArrayList<Part> parts;


    public ProductBlueprint(String productTitle, String description) {
        this.productTitle = productTitle;
        this.description = description;
    }
}