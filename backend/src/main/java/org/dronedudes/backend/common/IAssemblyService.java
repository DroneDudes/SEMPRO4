package org.dronedudes.backend.common;
import org.dronedudes.backend.Blueprint.Blueprint;
import org.dronedudes.backend.item.Item;

import java.util.List;
import java.util.UUID;

public interface IAssemblyService {
    UUID getAvailableAssemblyId();
    List<UUID> getAllAssemblyStationUuids();
    boolean assembleItem(UUID availableAssemblyStationUuid, Blueprint blueprint);
    Item getFinishedProductInAssemblyStation(UUID assemblyStationId);
}