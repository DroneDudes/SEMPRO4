package org.dronedudes.backend.Assembly.log;

import org.dronedudes.backend.agv.log.AgvLogEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssemblyLogRepository extends JpaRepository<AssemblyLog, Long> {
    List<AssemblyLog> findTop10ByOrderByTimestampDesc();
}
