package org.dronedudes.backend.common;

import java.util.UUID;

public interface PublisherInterface {
    void notifyChange(UUID machineId);
}
