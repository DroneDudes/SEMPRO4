package org.dronedudes.backend.Blueprint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Konkret implementation af Repository metoder
 */
@Service
public class BlueprintService {

    private final BlueprintRepository blueprintRepository;

    @Autowired
    public BlueprintService(BlueprintRepository blueprintRepository) {
        this.blueprintRepository = blueprintRepository;
    }

    public void saveBlueprint(ProductBlueprint blueprint) {
        blueprintRepository.save(blueprint);
    }

    public List<ProductBlueprint> getAll() {
        return blueprintRepository.findAll();
    }
}
