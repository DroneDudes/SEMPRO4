package org.dronedudes.backend.agv.log;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgvLogEntryRepository extends JpaRepository<AgvLogEntry, Long> {
        List<AgvLogEntry> findTop10ByOrderByTimestampDesc();



}
