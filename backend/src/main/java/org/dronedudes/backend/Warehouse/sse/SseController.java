package org.dronedudes.backend.Warehouse.sse;

import org.dronedudes.backend.Warehouse.WarehouseService;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse/v1/warehouses")
public class SseController {
    //private final WarehouseService warehouseService;
    private final SseEmitterManager sseEmitterManager;


    public SseController(SseEmitterManager sseEmitterManager) {
        this.sseEmitterManager = sseEmitterManager;
    }


    @GetMapping("/subscribe/{subscriberId}")
    public SseEmitter streamSse(@PathVariable String subscriberId) {
        SseEmitter emitter = new SseEmitter();
        sseEmitterManager.addEmitter(subscriberId, emitter);

        // Set a timeout for the SSE connection (optional)
        emitter.onTimeout(() -> {

            emitter.complete();
            sseEmitterManager.removeEmitter(subscriberId);
        });

        // Set a handler for client disconnect (optional)
        emitter.onCompletion(() -> {

            sseEmitterManager.removeEmitter(subscriberId);
        });

        return emitter;
    }
}
record SimpleResponse(String content) {

}
