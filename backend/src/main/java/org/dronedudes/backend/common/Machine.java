package org.dronedudes.backend.common;

import lombok.Data;
import java.util.UUID;

@Data
public abstract class Machine {
    private final UUID uuid;
    private final MachineType machineType;

    public Machine(MachineType machineType) {
        this.uuid = UUID.randomUUID();
        this.machineType = machineType;
    }

    public UUID getUuid() {
        return uuid;
    }
}
