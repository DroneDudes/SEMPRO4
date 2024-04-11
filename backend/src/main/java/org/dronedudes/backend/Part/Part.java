package org.dronedudes.backend.Part;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dronedudes.backend.item.Item;

@Entity
@Table(name = "parts")
@PrimaryKeyJoinColumn(name = "item_id")
@Data
@NoArgsConstructor
public class Part extends Item {
    private String description;

}
