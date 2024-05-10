package org.dronedudes.backend.Assembly.log;

import org.dronedudes.backend.agv.log.AgvLogEntry;
import org.dronedudes.backend.common.logging.LogEntry;
import org.dronedudes.backend.common.logging.LoggerInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AssemblyLogService implements LoggerInterface {

    @Autowired
    public AssemblyLogService(AssemblyLogRepository assemblyLogRepository) {
        this.assemblyLogRepository = assemblyLogRepository;
    }

    AssemblyLogRepository assemblyLogRepository;

    @Override
    public List<LogEntry> getLast10Logs() {
        List<AssemblyLog> logs = assemblyLogRepository.findTop10ByOrderByTimestampDesc();

        ArrayList<LogEntry> returnableLogs = new ArrayList<>();
        logs.forEach((logEntry -> returnableLogs.add(new LogEntry(logEntry.getTimestamp(), "Assembly_" + logEntry.getAssemblyStation().getId(), String.valueOf(logEntry.getState())))));
        return returnableLogs;
    }

    @Override
    public void publishNewLog(LogEntry logEntry) {

    }

    public void save(AssemblyLog assemblyLog){
        assemblyLogRepository.save(assemblyLog);
    }
}
