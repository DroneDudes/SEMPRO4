package org.dronedudes.backend.Assembly;

public enum State {
    IDLE(0,"Assembly is idle"),
    EXECUTING(1,"Assembly is executing"),
    ERROR(2,"An error occurred");

    private int state;
    private String description;
    State(int state, String description) {
        this.state = state;
        this.description = description;
    }
}
