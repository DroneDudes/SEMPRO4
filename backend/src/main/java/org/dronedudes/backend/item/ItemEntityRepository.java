package org.dronedudes.backend.item;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemEntityRepository extends JpaRepository<Item, Long> {
}
