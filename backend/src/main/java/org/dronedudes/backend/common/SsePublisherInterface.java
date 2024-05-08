package org.dronedudes.backend.common;

import org.dronedudes.backend.common.logging.LogEntry;

import java.util.UUID;

public interface SsePublisherInterface {
    LogEntry publishNewLog(UUID machineId);
}
