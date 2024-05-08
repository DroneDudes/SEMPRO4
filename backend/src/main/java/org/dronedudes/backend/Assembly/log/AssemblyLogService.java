package org.dronedudes.backend.Assembly.log;

import org.dronedudes.backend.common.logging.LogEntry;
import org.dronedudes.backend.common.logging.LoggerInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return List.of();
    }

    @Override
    public void publishNewLog(LogEntry logEntry) {

    }

    public void save(AssemblyLog assemblyLog){
        assemblyLogRepository.save(assemblyLog);
    }
}
