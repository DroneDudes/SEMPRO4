package org.dronedudes.backend.Part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @PostMapping("/createPart")
    public Part newPart(@RequestBody PartDTO partDTO) {
        return partService.createPart(partDTO);
    }

    @DeleteMapping("/delete/{partId}")
    public ResponseEntity<Void> deletePart(@PathVariable("partId") Long partId){
        partService.deletePartById(partId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{partId}")
    public Optional<Part> getPart(@PathVariable("partId") Long partId){
        return partService.getPartById(partId);
    }
}
