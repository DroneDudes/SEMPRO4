package org.dronedudes.backend.Assembly;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dronedudes.backend.common.Machine;
import org.dronedudes.backend.common.Item;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "assembly")
public class AssemblyStation extends Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String endpointUrl = "tcp://localhost:9001";

    @Transient
    private int state;

    @Transient
    private int processId;

    @Transient
    private String blueprintName;

    @Transient
    private Item product;
}
