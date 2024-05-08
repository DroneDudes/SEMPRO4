package org.dronedudes.backend.Assembly;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dronedudes.backend.common.Machine;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AssemblyStation extends Machine {

    @Id
    private Long id;
    @Transient
    private int state;


    public AssemblyStation(int state){
        this.state = state;
    }
}
