package org.dronedudes.backend.Part;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/parts")
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

    @PostConstruct
    public Part testPart() {
        Part part = new Part("1","1","1","1",1);
        partService.createPart(part);

        Part part1 = new Part("2","2","2","2",2);
        partService.createPart(part1);
        return part;
    }
}
