package org.dronedudes.backend.Assembly.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssemblyLogRepository extends JpaRepository<AssemblyLog, Long> {
}
