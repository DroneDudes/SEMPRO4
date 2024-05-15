package org.dronedudes.backend.common;

public interface IMachineOrchestrator {
    void startProduction(int amount, IBlueprint IBlueprint);
    void stopProduction();
}
