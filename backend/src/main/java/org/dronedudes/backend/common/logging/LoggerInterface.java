package org.dronedudes.backend.common.logging;

import java.util.List;

public interface LoggerInterface {
    public List<LogEntry> getLast10Logs();
}
