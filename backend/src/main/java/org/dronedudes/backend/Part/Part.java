package org.dronedudes.backend.Part;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dronedudes.backend.Blueprint.Blueprint;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Part {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private long id;
   @Column(unique = true)
   private String name;
   private String description;
   private String specifications;
   private String supplierDetails;
   private long price;

   @ManyToMany(mappedBy = "blueprintParts", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
   private Set<Blueprint> blueprints = new HashSet<>();

   public Part(String name, String description, String specifications, String supplierDetails, long price) {
      this.name = name;
      this.description = description;
      this.specifications = specifications;
      this.supplierDetails = supplierDetails;
      this.price = price;
   }
}
