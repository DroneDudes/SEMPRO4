package org.dronedudes.backend.agv.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface AgvLogEntryRepository extends JpaRepository<AgvLogEntry, Long> {
        List<AgvLogEntry> findFirst10ByAgv_Id(Long agvId);

}
