package org.dronedudes.backend.Part;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dronedudes.backend.common.Item;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import org.dronedudes.backend.Blueprint.Blueprint;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parts")
@PrimaryKeyJoinColumn(name = "item_id")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Part extends Item {

   private String description;
   private String specifications;
   private String supplierDetails;
   private long price;


   @ManyToMany(fetch = FetchType.LAZY,
           cascade = {
               CascadeType.PERSIST,
               CascadeType.MERGE
           },
           mappedBy = "parts")
   @JsonIgnore
   private List<Blueprint> blueprints = new ArrayList<>();

   public Part(String name, String description, String specifications, String supplierDetails, long price) {
      this.setName(name);
      this.description = description;
      this.specifications = specifications;
      this.supplierDetails = supplierDetails;
      this.price = price;
   }
}
