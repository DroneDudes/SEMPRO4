package org.dronedudes.backend.Assembly;

import lombok.Getter;

@Getter
public enum AssemblyStateEnum {

    IDLE(0,"Assembly is idle"),
    EXECUTING(1,"Assembly is executing"),
    ERROR(2,"An error occurred");

    private int state;
    private String description;

    AssemblyStateEnum(int state, String description) {
        this.state = state;
        this.description = description;
    }
}


