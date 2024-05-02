package org.dronedudes.backend.common.logging;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/logs")
@AllArgsConstructor
public class LoggerController {
    private final LoggerService loggerService;
    @GetMapping("/last10logs")
    public List<LogEntry> getLast10SortedLogs() {
        return this.loggerService.fetchLast10LogsFromAllInterfaceImplementationsAndSort();
    }

}