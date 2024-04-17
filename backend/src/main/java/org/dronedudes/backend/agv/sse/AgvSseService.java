package org.dronedudes.backend.agv.sse;

import lombok.RequiredArgsConstructor;
import org.dronedudes.backend.agv.AgvService;
import org.dronedudes.backend.common.SubscriberInterface;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class AgvSseService implements SubscriberInterface {

    private final AgvService agvService;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
    }
    public void update(UUID machineId) {
        for (SseEmitter emitter: emitters) {
            try {
                emitter.send(agvService.getAgvMap().get(machineId));
            } catch (IOException e) {
                emitter.complete();
                emitters.remove(emitter);
            }
        }
    }
}
