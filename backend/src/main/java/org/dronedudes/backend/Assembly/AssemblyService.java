package org.dronedudes.backend.Assembly;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.lang.Thread.sleep;

@Service
public class AssemblyService {


    private final AssemblyRepository assemblyRepository;


    @Autowired
    public AssemblyService(AssemblyRepository assemblyRepository) {
        this.assemblyRepository = assemblyRepository;
    }

    public void save(AssemblyStation assemblyStation) {
        assemblyRepository.save(assemblyStation);
    }

    public Optional<AssemblyStation> getAssemblyStationById(Long id) {
    return assemblyRepository.findById(id);
    }
}
