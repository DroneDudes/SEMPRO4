package org.dronedudes.backend.agv;

import org.dronedudes.backend.common.ObserverInterface;
import org.dronedudes.backend.common.SubscriberInterface;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class AgvObserverService implements ObserverInterface {
    private HashMap<Long, List<SubscriberInterface>> topicsSubscribersMap = new HashMap<>();

    @Override
    public void subscribe(Long agvId, SubscriberInterface subscriber) {
        List<SubscriberInterface> subscriberList = topicsSubscribersMap.get(agvId);
        subscriberList.add(subscriber);
        topicsSubscribersMap.put(agvId, subscriberList);
    }

    @Override
    public void unsubscribe(Long agvId, SubscriberInterface subscriber) {
        List<SubscriberInterface> subscriberList = topicsSubscribersMap.get(agvId);
        subscriberList.remove(subscriber);
        topicsSubscribersMap.put(agvId, subscriberList);
    }

    @Override
    public void updateSubscribers(Long agvId) {
        topicsSubscribersMap.computeIfAbsent(agvId, k -> new ArrayList<>());
        List<SubscriberInterface> subscriberList = topicsSubscribersMap.get(agvId);
        for (SubscriberInterface subscriber : subscriberList) {
            subscriber.update(agvId);
        }

    }

    public List<SubscriberInterface> getSubscribersForAgv(Long agvId) {
        return topicsSubscribersMap.get(agvId);
    }
}
