package org.dronedudes.backend.agv.sse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.dronedudes.backend.agv.Agv;
import org.dronedudes.backend.agv.AgvService;
import org.dronedudes.backend.common.ObserverService;
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
    private final ObserverService observerService;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
    }

    @PostConstruct
    @Override
    public void subscribe() {
        for (UUID machineId : agvService.getAgvMap().keySet()) {
            observerService.subscribe(machineId, this);
        }
    }

    @Override
    public void update(UUID machineId) {
        System.out.println("AgvSseService.update");
        Agv agv = agvService.getAgvMap().get(machineId);
        for (SseEmitter emitter: emitters) {
            try {
                SseEmitter.SseEventBuilder event = SseEmitter.event().data(agv).name("agvEvent");
                emitter.send(event);
            } catch (IOException e) {
                emitter.complete();
                emitters.remove(emitter);
            }
        }
    }
}
