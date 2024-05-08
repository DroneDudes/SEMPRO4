package org.dronedudes.backend.Warehouse.log;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseLogEntryRepository extends JpaRepository<WarehouseLogEntry, Long> {
    List<WarehouseLogEntry> findTop10ByOrderByTimestampDesc();
    WarehouseLogEntry findTopByOrderByTimestampDesc();
}
