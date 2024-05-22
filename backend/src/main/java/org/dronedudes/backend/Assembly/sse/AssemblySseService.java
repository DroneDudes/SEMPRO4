package org.dronedudes.backend.Assembly.sse;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.dronedudes.backend.Assembly.AssemblyService;
import org.dronedudes.backend.Assembly.AssemblyStation;
import org.dronedudes.backend.Assembly.log.AssemblyLogEntry;
import org.dronedudes.backend.agv.Agv;
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
public class AssemblySseService implements SubscriberInterface {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final AssemblyService assemblyService;
    private final ObserverService observerService;

    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
    }

    @Override
    @PostConstruct
    public void subscribe() {
        for(UUID machineId: assemblyService.getAssemblyMap().keySet()){
            observerService.subscribe(machineId, this);
        }
    }

    @Override
    public void update(UUID machineId) {
        System.out.println("AssemblySseService.update");
        AssemblyStation assemblyStation = assemblyService.getAssemblyMap().get(machineId);
        for (SseEmitter emitter: emitters) {
            try {
                SseEmitter.SseEventBuilder event = SseEmitter.event().data(assemblyStation).name("assemblyEvent");
                System.out.println("SSE Event " + event);
                emitter.send(event);
            } catch (IOException e) {
                emitter.complete();
                emitters.remove(emitter);
            }
        }
    }
}
