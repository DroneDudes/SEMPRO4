package org.dronedudes.backend.common.sse;

import lombok.RequiredArgsConstructor;
import org.dronedudes.backend.common.logging.LogEntry;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class SseService {
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
    }

    public void update(LogEntry logEntry) {
        for (SseEmitter emitter: emitters) {
            try {
                SseEmitter.SseEventBuilder event = SseEmitter.event().data(logEntry).name("LogEntry");
                emitter.send(event);
            } catch (IOException e) {
                emitter.complete();
                emitters.remove(emitter);
            }
        }
    }
}
