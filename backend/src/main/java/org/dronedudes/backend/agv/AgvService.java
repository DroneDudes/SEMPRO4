package org.dronedudes.backend.agv;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dronedudes.backend.agv.log.AgvLogEntryService;
import org.dronedudes.backend.agv.program.AgvProgramEnum;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
public class AgvService {
    private final AgvRepository agvRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private Agv agv;
    private final AgvLogEntryService agvLogEntryService;
    private Map<Long, Agv> agvMap = new HashMap<>();


    @PostConstruct
    public void fetchAllSystemAgvs() {
        for (Agv agv: agvRepository.findAll()) {
            agvMap.put(agv.getId(), agv);
        }
    }
    public Optional<Agv> returnSingleAgv() {
        return agvRepository.findFirstByOrderById();
    }

    public void logAgvStatus(Long agvId) {
        agvLogEntryService.logAgvStatus(agvMap.get(agvId));
    }
/*
    public void giveComand(AgvProgramEnum program, Long agvId) {
        agvMap.get(agvId).setAgvProgram();
    }
    */
}
