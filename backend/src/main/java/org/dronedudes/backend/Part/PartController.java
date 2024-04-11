package org.dronedudes.backend.Part;

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

    @PostMapping("/create")
    public Part newPart(@RequestBody Part part){
        return partService.createPart(part);
    }

    @GetMapping("/allparts")
    public List<Part> getAllParts(){
        return partService.findAllParts();
    }

    @GetMapping("/{partId}")
    public Optional<Part> getPart(@PathVariable("partId") Long partId){
        return partService.getPartById(partId);
    }

}
