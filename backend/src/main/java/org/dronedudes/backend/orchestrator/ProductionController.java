package org.dronedudes.backend.orchestrator;

import org.dronedudes.backend.common.IBlueprint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orchestrator")
public class ProductionController {

    private final ProductionOrchestrator productionOrchestrator;

    @Autowired
    public ProductionController(ProductionOrchestrator productionOrchestrator) {
        this.productionOrchestrator = productionOrchestrator;
    }

    @PostMapping("/startProduction")
    public ResponseEntity<String> startProduction(@RequestParam int amount, @RequestBody IBlueprint blueprint) {
        productionOrchestrator.startProduction(amount, blueprint);
        return ResponseEntity.ok("Production started successfully");
    }
}
