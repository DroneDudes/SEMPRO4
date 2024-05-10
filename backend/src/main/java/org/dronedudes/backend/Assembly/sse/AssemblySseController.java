package org.dronedudes.backend.Assembly.sse;

import lombok.RequiredArgsConstructor;
import org.dronedudes.backend.agv.sse.AgvSseService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/assembly")
@RequiredArgsConstructor
public class AssemblySseController {
    private final AssemblySseService assemblySseService;
    @GetMapping(path = "/assemblysse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        assemblySseService.addEmitter(emitter);
        return emitter;
    }
}