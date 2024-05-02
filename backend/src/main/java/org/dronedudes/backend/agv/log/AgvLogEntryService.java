package org.dronedudes.backend.agv.log;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.*;
import org.dronedudes.backend.agv.Agv;
import org.dronedudes.backend.agv.AgvService;
import org.dronedudes.backend.common.ObserverService;
import org.dronedudes.backend.common.SubscriberInterface;
import org.dronedudes.backend.common.logging.LogEntry;
import org.dronedudes.backend.common.logging.LoggerInterface;
import org.dronedudes.backend.common.sse.SseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Getter
@Transactional
public class AgvLogEntryService implements SubscriberInterface, LoggerInterface {
    private final AgvLogEntryRepository agvLogEntryRepository;
    private final ObserverService observerService;
    private final AgvService agvService;
    private final SseService sseService;
    private LogEntry newestLog;

    @PostConstruct
    public void subscribe() {
        for (Map.Entry<UUID, Agv> agvEntry : agvService.getAgvMap().entrySet()) {
            observerService.subscribe(agvEntry.getValue().getUuid(), this);
        }
    }

    public List<AgvLogEntry> return10NewestAgvLogs() {
        return agvLogEntryRepository.findTop10ByOrderByTimestampDesc();
    }


    @Override
    public List<LogEntry> getLast10Logs() {
        List<AgvLogEntry> fullLogs = this.agvLogEntryRepository.findTop10ByOrderByTimestampDesc();
        List<LogEntry> returnableLogs = new ArrayList<>();
        fullLogs.forEach((logEntry -> {
            returnableLogs.add(new LogEntry(logEntry.getTimestamp(), logEntry.getAgv().getName(), logEntry.getAgvProgram()));
        }));
        return returnableLogs;
    }

    @Override
    public void publishNewLog(LogEntry logEntry) {
        this.sseService.update(logEntry);
    }

    @Override
    public void update(UUID agvId) {
        Agv updatedAgv = agvService.getAgvMap().get(agvId);
        AgvLogEntry agvLogEntry = agvLogEntryRepository.save(new AgvLogEntry(
                updatedAgv.getBattery(),
                updatedAgv.getAgvProgram(),
                updatedAgv.getAgvState(),
                updatedAgv
        ));
        newestLog = new LogEntry(agvLogEntry.getTimestamp(), updatedAgv.getName(), updatedAgv.getAgvProgram().getProgramName());
        this.publishNewLog(newestLog);
    }
}
