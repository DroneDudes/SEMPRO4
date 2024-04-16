package org.dronedudes.backend.common;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ObserverService {
    private HashMap<Long, List<SubscriberInterface>> topicsSubscribersMap = new HashMap<>();

    public void subscribe(Long topicId, SubscriberInterface subscriber) {
        List<SubscriberInterface> subscriberList = topicsSubscribersMap.get(topicId);
        subscriberList.add(subscriber);
        topicsSubscribersMap.put(topicId, subscriberList);
    }

    public void unsubscribe(Long topicId, SubscriberInterface subscriber) {
        List<SubscriberInterface> subscriberList = topicsSubscribersMap.get(topicId);
        subscriberList.remove(subscriber);
        topicsSubscribersMap.put(topicId, subscriberList);
    }

    public void updateSubscribers(Long topicId) {
        topicsSubscribersMap.computeIfAbsent(topicId, k -> new ArrayList<>());
        List<SubscriberInterface> subscriberList = topicsSubscribersMap.get(topicId);
        for (SubscriberInterface subscriber : subscriberList) {
            subscriber.update(topicId);
        }

    }

    public List<SubscriberInterface> getSubscribersForAgv(Long topicId) {
        return topicsSubscribersMap.get(topicId);
    }

}
