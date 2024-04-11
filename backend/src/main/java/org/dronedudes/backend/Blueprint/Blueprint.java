package org.dronedudes.backend.Blueprint;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dronedudes.backend.Part.Part;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blueprint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String productTitle;
    private String description;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "blueprint_parts",
            joinColumns = @JoinColumn(name = "blueprint_id"),
            inverseJoinColumns = @JoinColumn(name = "part_id")
    )
    private Set<Part> blueprintParts = new HashSet<>();


    public Blueprint(String productTitle, String description) {
        this.productTitle = productTitle;
        this.description = description;
    }
}