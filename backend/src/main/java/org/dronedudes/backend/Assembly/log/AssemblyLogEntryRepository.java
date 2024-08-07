package org.dronedudes.backend.Assembly.log;

import org.dronedudes.backend.agv.log.AgvLogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssemblyLogEntryRepository extends JpaRepository<AssemblyLogEntry, Long> {
    List<AssemblyLogEntry> findTop10ByOrderByTimestampDesc();
    AssemblyLogEntry findTopByOrderByTimestampDesc();
}
