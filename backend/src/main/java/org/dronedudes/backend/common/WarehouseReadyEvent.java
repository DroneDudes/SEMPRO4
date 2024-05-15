package org.dronedudes.backend.common;

import org.springframework.context.ApplicationEvent;

public class WarehouseReadyEvent extends ApplicationEvent {
    public WarehouseReadyEvent(Object source) {
        super(source);
    }
}
