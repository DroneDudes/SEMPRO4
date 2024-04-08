package org.dronedudes.backend.agv;

import java.util.Arrays;
import java.util.Optional;

public enum AgvStateEnum {
    IDLE_STATE("1", "Idle"),
    EXECUTING_STATE("2", "Executing"),
    CHARGING_STATE("3", "Charging");
    private final String state;
    private final String description;

    AgvStateEnum(String state, String description) {
        this.state = state;
        this.description = description;
    }

    public static Optional<AgvStateEnum> getAgvStateByValue(String value) {
        return Arrays.stream(AgvStateEnum.values())
                .filter(state -> state.state.equals(value)
                        || state.description.equals(value))
                .findFirst();
    }

    public String getDescription() {
        return this.description;
    }
    public String getState() {
        return this.state;
    }
}
