package org.dronedudes.backend.agv.log;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*") //TODO: CHANGE CORS FOR SECURITY
@RequestMapping("/api/v1/agv")
@AllArgsConstructor
public class AgvLogEntryController {
    private final AgvLogEntryService agvLogEntryService;
    @GetMapping("/logs")
    public List<AgvLogEntry> getAgvLogEntries() {
        return agvLogEntryService.return10NewestAgvLogs();
    }

}
