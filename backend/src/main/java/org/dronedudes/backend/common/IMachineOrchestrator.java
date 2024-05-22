package org.dronedudes.backend.common;

import java.util.UUID;

public interface IMachineOrchestrator {
    void startProduction(int amount, IBlueprint IBlueprint);
    void stopProduction(UUID productionId);
}
