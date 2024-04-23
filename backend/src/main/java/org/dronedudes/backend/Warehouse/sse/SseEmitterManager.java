package org.dronedudes.backend.Warehouse.sse;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
@Service
public class SseEmitterManager {

    private final Map<String, SseEmitter> emitters = new HashMap<>();

    public void addEmitter(String subscriberId, SseEmitter emitter) {
        emitters.put(subscriberId, emitter);
    }

    public void removeEmitter(String subscriberId) {
        emitters.remove(subscriberId);
    }

    public void sendSseEventToClients(String data) {
        for (Map.Entry<String, SseEmitter> entry: emitters.entrySet()) {
            try {
                entry.getValue().send(data);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
