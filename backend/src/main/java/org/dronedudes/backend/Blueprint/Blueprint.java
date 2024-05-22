package org.dronedudes.backend.Blueprint;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dronedudes.backend.Part.Part;
import org.dronedudes.backend.common.IBlueprint;
import org.dronedudes.backend.common.IPart;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Blueprint implements IBlueprint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String productTitle;
    private String description;

    @ManyToMany(fetch = FetchType.LAZY,
        cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
        })
    @JoinTable(
            name = "blueprintParts",
            joinColumns = @JoinColumn(name = "blueprint_id"),
            inverseJoinColumns = @JoinColumn(name = "part_id")
    )
    private Set<Part> parts = new HashSet<>();
    /*
    public void addPart(Part part) {
        if (this.parts == null) {
            this.parts = new HashSet<>() {
            };
        }
        this.parts.add(part);
    }
    */

    @Override
    public void addPart(IPart IPart) {
        if (this.parts == null) {
            this.parts = new HashSet<>() {
            };
        }
        this.parts.add((Part) IPart);
    }
    @Override
    public Set<IPart> getParts() {
        return parts.stream().map(part -> (IPart) part).collect(Collectors.toSet());
    }
}