package org.dronedudes.backend.common;

public interface ObserverInterface {
    void subscribe(Long machineId, SubscriberInterface subscriber);
    void unsubscribe(Long machineId, SubscriberInterface subscriber);
    void updateSubscribers(Long machineId);
}
