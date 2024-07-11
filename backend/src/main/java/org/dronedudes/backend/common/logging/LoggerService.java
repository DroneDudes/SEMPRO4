package org.dronedudes.backend.common.logging;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import static java.util.stream.Collectors.toList;

@Service
@Data
@Getter
public class LoggerService {
    @Autowired
    private ApplicationContext applicationContext;
    private Map<String, LoggerInterface> loggerInterfaceImplementations;
    private List<LogEntry> newlyFetchedLogs = new ArrayList<>();
    private List<LogEntry> newestLogsSorted = new ArrayList<>();

    @PostConstruct
    private void getApplicationContextFromSpringBoot() {
        this.loggerInterfaceImplementations = applicationContext.getBeansOfType(LoggerInterface.class);
    }

    public List<LogEntry> fetchLast10LogsFromAllInterfaceImplementationsAndSort() {
        for (LoggerInterface loggerImplementation : loggerInterfaceImplementations.values()) {
            this.newlyFetchedLogs.addAll(loggerImplementation.getLast10Logs());
        }
        this.newestLogsSorted = this.newlyFetchedLogs.stream()
                .sorted(Comparator.comparing(LogEntry::getTimestamp).reversed())
                .limit(10)
                .collect(toList());
        this.newlyFetchedLogs.clear();
        return this.newestLogsSorted;
    }
}
