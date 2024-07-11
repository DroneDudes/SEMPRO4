package org.dronedudes.backend.Blueprint;

import org.dronedudes.backend.Part.Part;
import org.dronedudes.backend.Part.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * Konkret implementation af Repository metoder
 */
@Service
public class BlueprintService {

    private final BlueprintRepository blueprintRepository;
    private final PartRepository partRepository;
    @Autowired
    public BlueprintService(BlueprintRepository blueprintRepository, PartRepository partRepository) {
        this.blueprintRepository = blueprintRepository;
        this.partRepository = partRepository;
    }

    @Transactional
    public Blueprint createAndSaveBlueprint(BlueprintCreateRequest createRequest) {
        Blueprint blueprint = new Blueprint();
        blueprint.setProductTitle(createRequest.getProductTitle());
        blueprint.setDescription(createRequest.getDescription());
        System.out.println(createRequest.getPartsList());
        for (Long partID : createRequest.getPartsList()) {
            Optional<Part> partOpt = partRepository.findById(partID);
            System.out.println(partOpt.get().getId());
            if (partOpt.isPresent()) {
                Part part = partOpt.get();
                blueprint.getParts().add(part);
            }
        }
        blueprintRepository.save(blueprint);
        return blueprint;
    }

    public Blueprint saveBlueprint(Blueprint blueprint) {
        return blueprintRepository.save(blueprint);
    }

    public List<Blueprint> getAll() {
        return blueprintRepository.findAll();
    }

    public Blueprint getById(Long Id) {
        return blueprintRepository.findById(Id).orElse(null);
    }

    public List<Blueprint> getBlueprintsByPartId(Long Id) {
        return blueprintRepository.findBlueprintsByPartsId(Id);
    }

    public void deleteBlueprintById(Long blueprintId) {
        blueprintRepository.deleteById(blueprintId);
    }

}
