package org.dronedudes.backend.common;

import java.util.List;
import java.util.UUID;

public interface IAssemblyService {
    UUID getAvailableAssemblyId();
    List<UUID> getAllAssemblyStationUuids();
    boolean assembleItem(UUID availableAssemblyStationUuid, Item item);
    Item getFinishedProductInAssemblyStation(UUID assemblyStationId);
}
