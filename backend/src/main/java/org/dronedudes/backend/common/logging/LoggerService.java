package org.dronedudes.backend.common.logging;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 2021-10-10 10:10:10 example

    /*
    @PostConstruct
    private void createDummyLogs() {
        List<LogEntry> dummyList = new ArrayList<>();
        Random random = new Random();
        System.out.println("Creating dummy logs");
        for (int i = 0; i < 12; i++) {
            String time = "2021-10-10 10:10:" + random.nextInt(60);
            dummyList.add(new LogEntry(time, "Dummy"+i, "Dummy"+i));
        }
        this.fetchLast10LogsFromAllInterfaceImplementationsAndSort();
        //this.newestLogsSorted.forEach(System.out::println);
    }
     */

    @PostConstruct
    private void getApplicationContextFromSpringBoot() {
        this.loggerInterfaceImplementations = applicationContext.getBeansOfType(LoggerInterface.class);
    }

    public List<LogEntry> fetchLast10LogsFromAllInterfaceImplementationsAndSort() {
        System.out.println("Fetching last 10 logs from " + loggerInterfaceImplementations.size() + " implementations of LoggerInterface");
        for (LoggerInterface loggerImplementation : loggerInterfaceImplementations.values()) {
            System.out.println("Fetching logs from " + loggerImplementation.getClass().getSimpleName());
            this.newlyFetchedLogs.addAll(loggerImplementation.getLast10Logs());
        }
        System.out.println(this.newlyFetchedLogs);
        this.newestLogsSorted = this.newlyFetchedLogs.stream()
                .sorted(Comparator.comparing(LogEntry::getTimestamp).reversed())
                .limit(10)
                .collect(toList());
        this.newlyFetchedLogs.clear();
        System.out.println("Size of 10 newest logs sorted: " + newestLogsSorted.size() + " (should be 10 or less)");
        this.newestLogsSorted.forEach(System.out::println);
        return this.newestLogsSorted;
    }
}
