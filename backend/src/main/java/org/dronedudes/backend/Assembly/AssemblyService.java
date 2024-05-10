package org.dronedudes.backend.Assembly;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.lang.Thread.sleep;

@Service
@Transactional
public class AssemblyService {

    private final AssemblyRepository assemblyRepository;
    @Getter
    private Map<UUID, AssemblyStation> assemblyMap = new HashMap<>();

    @PostConstruct
    public void retrieveAssemblyStations(){
        for(AssemblyStation assemblyStation: assemblyRepository.findAllByOrderByIdDesc()){
            assemblyMap.put(assemblyStation.getUuid(),assemblyStation);
            System.out.println(assemblyStation);
        }
    }

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
