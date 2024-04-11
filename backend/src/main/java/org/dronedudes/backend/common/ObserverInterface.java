package org.dronedudes.backend.common;

public interface ObserverInterface {
    void subscribe(Long agvId, SubscriberInterface subscriber);
    void unsubscribe(Long agvId, SubscriberInterface subscriber);
    void updateSubscribers(Long agvId);
}
