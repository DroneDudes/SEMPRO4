package org.dronedudes.backend.Warehouse;

import org.dronedudes.backend.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    @Query("SELECT w.items FROM Warehouse w WHERE w.id = :warehouseId")
    List<Item> getWarehouseInventory(Long warehouseId);
}
