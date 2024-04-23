package org.dronedudes.backend.Warehouse.sse;

import org.dronedudes.backend.Warehouse.WarehouseService;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/sse/v1/warehouses")
public class SseController {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final long TIMEOUT_HALF_AN_HOUR = 1800000;



    @GetMapping
    public SseEmitter streamSse() {
        String id = UUID.randomUUID().toString();
        SseEmitter emitter = new SseEmitter(TIMEOUT_HALF_AN_HOUR);
        emitters.put(id, emitter);
        // Set a timeout for the SSE connection (optional)
        emitter.onTimeout(() -> {

            emitter.complete();
            emitters.remove(id);
        });

        // Set a handler for client disconnect (optional)
        emitter.onCompletion(() -> {

            emitters.remove(id);
        });

        return emitter;
    }

    @EventListener
    public void onSseWarehouseUpdate(SseWarehouseUpdateEvent event) {
        Map<String, SseEmitter> deadEmitters = new HashMap<>();
        emitters.forEach((id, emitter) -> {
            try {
                emitter.send(event.getWarehouses());
                System.out.println("Sending data");
            } catch (Exception e) {
                deadEmitters.put(id, emitter);
            }
        });

        deadEmitters.forEach((id, emitter) -> {
            emitter.complete();
            emitters.remove(id);
        });

    }
}

