package org.dronedudes.backend.Part;


import org.dronedudes.backend.Blueprint.Blueprint;
import org.dronedudes.backend.Blueprint.BlueprintCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<Part> getAll() {
        return partRepository.findAll();
    }

    public Optional<Part> getPartById(Long partId){
        return partRepository.findById(partId);
    }

    @Transactional
    public Part createPart(PartDTO partDTO) {
        Part part = new Part();
        part.setName(partDTO.getName());
        part.setDescription(partDTO.getDescription());
        part.setSpecifications(partDTO.getSpecifications());
        part.setSupplierDetails(partDTO.getSpecifications());
        part.setPrice(partDTO.getPrice());

        partRepository.save(part);

        return part;
    }

    public void deletePart(Long partId) {
        partRepository.deleteById(partId);
    }
}
