package org.dronedudes.backend.Assembly.log;

import org.dronedudes.backend.common.logging.LogEntry;
import org.dronedudes.backend.common.logging.LoggerInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AssemblyLogEntryService implements LoggerInterface {

    @Autowired
    public AssemblyLogEntryService(AssemblyLogEntryRepository assemblyLogEntryRepository) {
        this.assemblyLogEntryRepository = assemblyLogEntryRepository;
    }

    AssemblyLogEntryRepository assemblyLogEntryRepository;

    @Override
    public List<LogEntry> getLast10Logs() {
        List<AssemblyLogEntry> logs = assemblyLogEntryRepository.findTop10ByOrderByTimestampDesc();

        ArrayList<LogEntry> returnableLogs = new ArrayList<>();
        logs.forEach((logEntry -> returnableLogs.add(new LogEntry(logEntry.getTimestamp(), "Assembly_" + logEntry.getAssemblyStation().getId(), String.valueOf(logEntry.getState())))));
        return returnableLogs;
    }

    @Override
    public void publishNewLog(LogEntry logEntry) {

    }

    public void save(AssemblyLogEntry assemblyLogEntry){
        assemblyLogEntryRepository.save(assemblyLogEntry);
    }
}
