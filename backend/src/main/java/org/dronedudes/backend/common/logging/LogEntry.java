package org.dronedudes.backend.common.logging;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class LogEntry {
    private String timestamp;
    private String name;
    private String action;

    public LogEntry(String timestamp, String name, String action) {
        this.timestamp = timestamp;
        this.name = name;
        this.action = action;
    }
}
