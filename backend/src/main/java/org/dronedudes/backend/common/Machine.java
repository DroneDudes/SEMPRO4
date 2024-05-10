package org.dronedudes.backend.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public abstract class Machine {
    private UUID uuid;
    private MachineType machineType;

    public Machine(MachineType machineType) {
        this.uuid = UUID.randomUUID();
        this.machineType = machineType;
    }

}
