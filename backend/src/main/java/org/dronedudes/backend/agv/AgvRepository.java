package org.dronedudes.backend.agv;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AgvRepository extends JpaRepository<Agv, Long> {
    Optional<Agv> findFirstByOrderById();
    Optional<Agv> findFirstById(Long id);
}
