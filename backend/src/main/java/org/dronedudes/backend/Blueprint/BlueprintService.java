package org.dronedudes.backend.Blueprint;

import org.dronedudes.backend.Part.Part;
import org.dronedudes.backend.Part.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    /*
    @Transactional()
    public boolean addPartToBlueprint(long blueprint_ID,long part_ID) {
        Optional<Blueprint> blueprintOpt = blueprintRepository.findById(blueprint_ID);
        Optional<Part> partOpt = partRepository.findById(part_ID);

        if (blueprintOpt.isPresent() && partOpt.isPresent()) {
            Blueprint blueprint = blueprintOpt.get();
            Part part = partOpt.get();
            blueprint.getBlueprintParts().add(part);
            System.out.println(blueprint);
            System.out.println(blueprintRepository.save(blueprint));
            return true;
        } else {
            return false;
        }
    }

     */
}
