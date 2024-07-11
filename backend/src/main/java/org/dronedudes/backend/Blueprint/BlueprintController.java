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

    @GetMapping("/{blueprintId}")
    public Blueprint getBlueprintById(@PathVariable (value = "blueprintId") Long blueprintId) {
        return blueprintService.getById(blueprintId);
    }

    @GetMapping("/part/{partId}")
    public List<Blueprint> getBlueprintsByPartId(@PathVariable (value = "partId") Long partId) {
        return blueprintService.getBlueprintsByPartId(partId);
    }
  
    @PostMapping("/create")
    public Blueprint newBlueprint(@RequestBody BlueprintCreateRequest createRequest) {
        return blueprintService.createAndSaveBlueprint(createRequest);
    }

    @DeleteMapping("/delete/{blueprintId}")
    public ResponseEntity<Void> deleteBlueprint(@PathVariable("blueprintId") Long blueprintId){
        blueprintService.deleteBlueprintById(blueprintId);
        return ResponseEntity.ok().build();

    }
}

