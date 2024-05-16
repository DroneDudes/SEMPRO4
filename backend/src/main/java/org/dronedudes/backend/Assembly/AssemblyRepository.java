package org.dronedudes.backend.Assembly;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssemblyRepository extends JpaRepository<AssemblyStation, Long> {

    List<AssemblyStation> findAllByOrderByIdDesc();

    Optional<AssemblyStation> findFirstByOrderById();
}
