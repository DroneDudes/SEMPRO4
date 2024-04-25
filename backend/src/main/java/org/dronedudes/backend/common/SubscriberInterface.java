package org.dronedudes.backend.common;

import java.util.UUID;

public interface SubscriberInterface {
    void update(UUID machineId);
}
