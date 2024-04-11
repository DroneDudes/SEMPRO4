package org.dronedudes.backend.agv.log;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dronedudes.backend.agv.Agv;
import org.dronedudes.backend.agv.AgvObserverService;
import org.dronedudes.backend.agv.AgvService;
import org.dronedudes.backend.common.SubscriberInterface;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Getter
@Transactional
public class AgvLogEntryService implements SubscriberInterface {
    private final AgvLogEntryRepository agvLogEntryRepository;
    private final AgvObserverService agvObserverService;
    private final AgvService agvService;

    @PostConstruct
    public void subscribeToAgvObserverService() {
        for (Map.Entry<Long, Agv> agvEntry : agvService.getAgvMap().entrySet()) {
            agvObserverService.subscribe(agvEntry.getKey(), this);
        }
    }

    @Override
    public void update(Long agvId) {
        Agv updatedAgv = agvService.getAgvMap().get(agvId);
        agvLogEntryRepository.save(new AgvLogEntry(
                updatedAgv.getBattery(),
                updatedAgv.getAgvProgram(),
                updatedAgv.getAgvState(),
                updatedAgv
        ));
    }
}
