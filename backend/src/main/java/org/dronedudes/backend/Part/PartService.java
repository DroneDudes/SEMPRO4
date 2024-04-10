package org.dronedudes.backend.Part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartService {

    private final PartRepository partRepository;

    @Autowired
    public PartService(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    public Part createPart(Part part){
        partRepository.save(part);
        return part;
    }

    public List<Part> findAllParts() {
        return partRepository.findAll();
    }

    public Optional<Part> getPartById(Long partId){
        return partRepository.findById(partId);
    }
}
