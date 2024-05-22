package org.dronedudes.backend.Assembly.log;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/assembly")
@AllArgsConstructor
public class AssemblyLogEntryController {
    private final AssemblyLogEntryService assemblyLogEntryService;
//    @GetMapping("/logs")
//    public List<AssemblyLogEntry> getAgvLogEntries() {
//        return assemblyLogEntryService.return10NewestAgvLogs();
//    }

    @GetMapping("/lastlog")
    public AssemblyLogEntry getlastAgvLog() {
        return this.assemblyLogEntryService.getLastAssemblyLog();
    }
}
