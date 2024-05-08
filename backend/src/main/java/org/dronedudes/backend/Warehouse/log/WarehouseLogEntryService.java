package org.dronedudes.backend.Warehouse.log;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dronedudes.backend.Warehouse.Warehouse;
import org.dronedudes.backend.Warehouse.WarehouseService;
import org.dronedudes.backend.common.ObserverService;
import org.dronedudes.backend.common.SubscriberInterface;
import org.dronedudes.backend.common.logging.LogEntry;
import org.dronedudes.backend.common.logging.LoggerInterface;
import org.dronedudes.backend.common.sse.SseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Getter
@Transactional
public class WarehouseLogEntryService implements LoggerInterface {
    private final WarehouseLogEntryRepository warehouseLogEntryRepository;
    private final ObserverService observerService;
    private final SseService sseService;
    private LogEntry newestLog;



    public List<WarehouseLogEntry> return10NewestWarehouseLogs() {
        return warehouseLogEntryRepository.findTop10ByOrderByTimestampDesc();
    }

    @Override
    public List<LogEntry> getLast10Logs() {
        List<LogEntry> returnableLogs = new ArrayList<>();
        this.return10NewestWarehouseLogs().forEach((logEntry -> {
            returnableLogs.add(new LogEntry(logEntry.getTimestamp(), logEntry.getName(), logEntry.getAction()));
        }));
        return returnableLogs;
    }

    public void saveWarehouseLog(WarehouseLogEntry warehouseLogEntry) {
        warehouseLogEntryRepository.save(warehouseLogEntry);
    }

    @Override
    public void publishNewLog(LogEntry logEntry) {

    }
}
