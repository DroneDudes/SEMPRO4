package org.dronedudes.backend.agv.state;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgvStateRepository extends JpaRepository<AgvState, Long> {
}
