package org.dronedudes.backend.Warehouse;

import org.dronedudes.backend.common.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    @Query("SELECT w.items FROM Warehouse w WHERE w.id = :warehouseId")
    Map<Long, Item> getWarehouseInventory(Long warehouseId);
}
