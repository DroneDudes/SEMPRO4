package org.dronedudes.backend.common.logging;

import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LoggerServiceTest {

    private LoggerService loggerService;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private LoggerInterface loggerInterface1;

    @Mock
    private LoggerInterface loggerInterface2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Map<String, LoggerInterface> loggerInterfaceImplementations = new HashMap<>();
        loggerInterfaceImplementations.put("loggerInterface1", loggerInterface1);
        loggerInterfaceImplementations.put("loggerInterface2", loggerInterface2);

        when(applicationContext.getBeansOfType(LoggerInterface.class)).thenReturn(loggerInterfaceImplementations);

        loggerService = new LoggerService();
        loggerService.setApplicationContext(applicationContext);
        loggerService.setLoggerInterfaceImplementations(loggerInterfaceImplementations);
    }

    @Test
    void fetchLast10LogsFromAllInterfaceImplementationsAndSort() {
        List<LogEntry> logs1 = Arrays.asList(
                new LogEntry("2010-05-30 22:15:52", "Machine log first 1", "Started the machine log 1"),
                new LogEntry("2010-05-30 22:16:02", "Machine log last 1", "Ended the machine log 1")
        );

        List<LogEntry> logs2 = Arrays.asList(
                new LogEntry("2010-05-30 22:16:52", "Machine log last 2", "Ended the machine log 2"),
                new LogEntry("2010-05-30 22:17:02", "Machine log first 2", "Started the machine log 2")
        );

        when(loggerInterface1.getLast10Logs()).thenReturn(logs1);
        when(loggerInterface2.getLast10Logs()).thenReturn(logs2);



        List<LogEntry> result = loggerService.fetchLast10LogsFromAllInterfaceImplementationsAndSort();

        assertEquals(4, result.size());
        // Assert your sorting logic here if necessary
    }

    @Test
    void testLogSorting() {
        LogEntry log1 = new LogEntry("2010-05-30 23:15:52", "Machine log first 1", "Started the machine log 1");
        LogEntry log2 = new LogEntry("2010-05-30 22:16:02", "Machine log last 1", "Ended the machine log 1");
        LogEntry log3 = new LogEntry("2010-05-30 22:36:52", "Machine log last 2", "Ended the machine log 2");
        LogEntry log4 = new LogEntry("2010-05-30 22:17:02", "Machine log first 2", "Started the machine log 2");

        // Add the logs to the service
        ArrayList<LogEntry> logs = new ArrayList<>();
        logs.add(log1);
        logs.add(log2);
        logs.add(log3);
        logs.add(log4);

        ArrayList<LogEntry> sortedLogs = (ArrayList<LogEntry>) logs.stream()
                .sorted(Comparator.comparing(LogEntry::getTimestamp).reversed())
                .limit(10)
                .collect(toList());


        // Assert the sorting
        assertEquals(log4, sortedLogs.get(2));
        assertEquals(log3, sortedLogs.get(1));
        assertEquals(log2, sortedLogs.get(3));
        assertEquals(log1, sortedLogs.get(0));
    }
}
