package org.dronedudes.backend.Blueprint;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlueprintRepository extends JpaRepository<Blueprint,Long> {
    @Query("SELECT b FROM Blueprint b JOIN b.parts p WHERE p.id = :partId")
    List<Blueprint> findBlueprintsByPartsId(@Param("partId") Long id);

}
