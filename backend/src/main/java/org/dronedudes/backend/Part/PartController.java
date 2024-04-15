package org.dronedudes.backend.Part;

import jakarta.annotation.PostConstruct;
import org.dronedudes.backend.Blueprint.Blueprint;
import org.dronedudes.backend.Blueprint.BlueprintCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/parts")
@CrossOrigin(origins = "http://localhost:4200")
public class PartController {

    private final PartService partService;

    @Autowired
    public PartController(PartService partService) {
        this.partService = partService;
    }

    @GetMapping
    public List<Part> getAllParts() {
        return partService.getAll();
    }
    @PostMapping("/create")
    public Part newPart(@RequestBody Part part){
        return partService.createPart(part);
    }

    @PostMapping("/createFromAngular")
    public Part newBlueprint(@RequestBody PartDTO partDTO) {
        return partService.createPartFromAngular(partDTO);
    }

    @GetMapping("/blueprint/{blueprintId}")
    public List<Part> getAllPartsByBlueprintId(@PathVariable(value = "blueprintId") Long blueprintId) {
        return partService.findPartsByBlueprintId(blueprintId);
    }

    @PostConstruct
    public Part testPart() {
        Part part = new Part("1","1","1","1",1);
        partService.createPart(part);

        Part part1 = new Part("2","2","2","2",2);
        partService.createPart(part1);
        return part;
    }

    @GetMapping("/{partId}")
    public Optional<Part> getPart(@PathVariable("partId") Long partId){
        return partService.getPartById(partId);
    }

}