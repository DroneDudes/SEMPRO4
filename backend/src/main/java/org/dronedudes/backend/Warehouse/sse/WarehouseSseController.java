package org.dronedudes.backend.Warehouse.sse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
@Controller
public class WarehouseSseController
{
    private static final Logger logger = LoggerFactory.getLogger(WarehouseSseController.class);
    private final ConcurrentMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @GetMapping(value = "/sse/v1/warehouses", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamWarehouses() {
        String id = "warehouseStream";
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(id, emitter);

        emitter.onCompletion(() -> emitters.remove(id));
        emitter.onTimeout(() -> {
            emitter.complete();
            emitters.remove(id);
        });

        return emitter;
    }

    @EventListener
    public void onWarehouseUpdateEvent(WarehouseUpdateEvent event) {
        SseEmitter emitter = emitters.get("warehouseStream");
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("warehouse-update").data(event.getWarehouses()));
            } catch (Exception e) {
                logger.error("Error sending SSE for warehouses", e);
                emitter.completeWithError(e);
            }
        }
    }
}

