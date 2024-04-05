package org.dronedudes.backend.Blueprint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping
    public List<ProductBlueprint> getAllBlueprint() {
        /*
        uncomment for at lave blueprints i DB
        ProductBlueprint productBlueprint = new ProductBlueprint("1","1");
        blueprintService.saveBlueprint(productBlueprint);

         */
        return blueprintService.getAll();
    }
}
