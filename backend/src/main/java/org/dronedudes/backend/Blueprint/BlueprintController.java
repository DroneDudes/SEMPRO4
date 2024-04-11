package org.dronedudes.backend.Blueprint;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /*
    @PostConstruct
    public Blueprint blueprintTest(){
        Blueprint productBlueprint = new Blueprint("1","1");
        blueprintService.saveBlueprint(productBlueprint);
        return productBlueprint;
    }

     */

    @PutMapping("/{blueprintId}/parts/{partId}")
    public ResponseEntity<?> addPartToBlueprint(@PathVariable Long blueprintId, @PathVariable Long partId) {
        boolean success = blueprintService.addPartToBlueprint(blueprintId, partId);
        if (success) {
            return ResponseEntity.ok().body("Part added to blueprint successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to add part to blueprint.");
        }
    }

    @PostMapping("/create")
    public Blueprint newBlueprint(@RequestBody Blueprint blueprint) {
        return blueprintService.saveBlueprint(blueprint);
    }
}
