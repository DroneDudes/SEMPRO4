package org.dronedudes.backend.Part;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dronedudes.backend.item.Item;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Part extends Item {

    String partTitle;
    String description;
    @Id
    @GeneratedValue
    private Long id;

}
