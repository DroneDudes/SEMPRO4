package org.dronedudes.backend.Assembly;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dronedudes.backend.common.Machine;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class AssemblyStation extends Machine {

    private int state;

    public AssemblyStation(int state){
        this.state = state;
    }
}
