package org.dronedudes.backend.common;

import java.util.UUID;

public abstract class Machine {
    private final UUID uuid;

    public Machine() {
        this.uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }
}
