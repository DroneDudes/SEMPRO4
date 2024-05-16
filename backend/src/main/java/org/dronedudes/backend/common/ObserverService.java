package org.dronedudes.backend.common;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class ObserverService {
    private HashMap<UUID, List<SubscriberInterface>> topicsSubscribersMap = new HashMap<>();

    public void subscribe(UUID machineId, SubscriberInterface subscriber) {
        List<SubscriberInterface> subscriberList = topicsSubscribersMap.get(machineId);
        subscriberList.add(subscriber);
        topicsSubscribersMap.put(machineId, subscriberList);
    }

    public void unsubscribe(UUID machineId, SubscriberInterface subscriber) {
        List<SubscriberInterface> subscriberList = topicsSubscribersMap.get(machineId);
        subscriberList.remove(subscriber);
        topicsSubscribersMap.put(machineId, subscriberList);
    }

    public void updateSubscribers(UUID machineId) {
        topicsSubscribersMap.computeIfAbsent(machineId, k -> new ArrayList<>());
        List<SubscriberInterface> subscriberList = topicsSubscribersMap.get(machineId);
        for (SubscriberInterface subscriber : subscriberList) {
            subscriber.update(machineId);
            System.out.println("Subscriber updated" + subscriber.getClass() + " for topic " + machineId);
        }
    }


    public List<SubscriberInterface> getSubscribersForTopic(UUID machineId) {
        return topicsSubscribersMap.get(machineId);
    }

}
