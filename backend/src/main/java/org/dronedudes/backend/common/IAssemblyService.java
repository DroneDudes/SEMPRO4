package org.dronedudes.backend.common;

import java.util.UUID;

public interface IAssemblyService {
    UUID getAvailableAssemblyId();
    boolean assembleItem(UUID availableAssemblyStationUuid, Item item);


}
