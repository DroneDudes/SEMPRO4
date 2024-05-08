package org.dronedudes.backend.common;

import lombok.Data;
import java.util.UUID;

@Data
public abstract class Machine {
    private final UUID uuid;

    public Machine() {
        this.uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }
}
