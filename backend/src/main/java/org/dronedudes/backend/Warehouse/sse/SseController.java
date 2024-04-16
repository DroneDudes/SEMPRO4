package org.dronedudes.backend.Warehouse.sse;

import org.dronedudes.backend.Warehouse.Warehouse;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class SseController {
    private final Set<SseEmitter> emitters = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @GetMapping("/sse/warehouses")
    public SseEmitter handleWarehouseUpdates() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> {
            emitter.complete();
            emitters.remove(emitter);
        });

        return emitter;
    }

    @EventListener
    public void onWarehouseEvent(WarehouseEvent event) {
        List<Warehouse> warehouses = event.getWarehouses();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(warehouses, MediaType.APPLICATION_JSON);
            } catch (Exception e) {
                emitter.completeWithError(e);
                emitters.remove(emitter);
            }
        }
    }
}
