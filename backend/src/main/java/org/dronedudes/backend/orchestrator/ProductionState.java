package org.dronedudes.backend.orchestrator;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class ProductionState {
    private final UUID productionId;
    private final Map<Long, UUID> partsOnAgv = new HashMap<>();
    private final Map<Long, UUID> partsOnAssemblyStation = new HashMap<>();

    public ProductionState(UUID productionId) {
        this.productionId = productionId;
    }

    public void addPartOnAgv(Long partId, UUID agvId) {
        partsOnAgv.put(partId, agvId);
    }

    public void removePartFromAgv(Long partId) {
        partsOnAgv.remove(partId);
    }

    public void addPartOnAssemblyStation(Long partId, UUID assemblyStationId) {
        partsOnAssemblyStation.put(partId, assemblyStationId);
    }

    public void removePartFromAssemblyStation(Long partId) {
        partsOnAssemblyStation.remove(partId);
    }
}
