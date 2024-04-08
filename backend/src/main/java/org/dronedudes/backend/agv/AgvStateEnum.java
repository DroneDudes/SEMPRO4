package org.dronedudes.backend.agv;

import java.util.Arrays;
import java.util.Optional;

public enum AgvStateEnum {
    IDLE_STATE(1, "Idle"),
    EXECUTING_STATE(2, "Executing"),
    CHARGING_STATE(3, "Charging");
    private final int state;
    private final String description;

    AgvStateEnum(int state, String description) {
        this.state = state;
        this.description = description;
    }

    public static AgvStateEnum find(int state) {
        AgvStateEnum result = null;
        for (AgvStateEnum agvStateEnum : values()) {
            if (agvStateEnum.state == state) {
                result = agvStateEnum;
                break;
            }
        }
        return result;
    }

    public String getDescription() {
        return this.description;
    }
    public int getState() {
        return this.state;
    }
}
