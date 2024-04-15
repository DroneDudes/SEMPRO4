package org.dronedudes.backend.Blueprint;

import jakarta.annotation.PostConstruct;
import org.dronedudes.backend.Part.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Endpoint konfiguration.
 * Bruger BlueprintService's implementation til data på endpoints
 */
@RestController
@RequestMapping("api/v1/blueprints")
@CrossOrigin(origins = "http://localhost:4200")
public class BlueprintController {

    private final BlueprintService blueprintService;

    @Autowired
    public BlueprintController(BlueprintService blueprintService) {
        this.blueprintService = blueprintService;
    }

    @GetMapping("/all")
    public List<Blueprint> getAllBlueprints() {
        return blueprintService.getAll();
    }

    @PostMapping("/create")
    public Blueprint newBlueprint(@RequestBody BlueprintCreateRequest createRequest) {
        System.out.println(createRequest);
        return blueprintService.createAndSaveBlueprint(createRequest);
    }
}
