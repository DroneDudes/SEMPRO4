package org.dronedudes.backend.agv.log;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dronedudes.backend.agv.Agv;
import org.dronedudes.backend.agv.AgvService;
import org.dronedudes.backend.common.ObserverService;
import org.dronedudes.backend.common.SubscriberInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Getter
@Transactional
public class AgvLogEntryService implements SubscriberInterface {
    private final AgvLogEntryRepository agvLogEntryRepository;
    private final ObserverService observerService;
    private final AgvService agvService;

    @PostConstruct
    public void subscribe() {
        for (Map.Entry<UUID, Agv> agvEntry : agvService.getAgvMap().entrySet()) {
            observerService.subscribe(agvEntry.getValue().getUuid(), this);
        }
    }

    public List<AgvLogEntry> return10NewestAgvLogs() {
        return agvLogEntryRepository.findTop10ByOrderByTimestampDesc();
    }

    @Override
    public void update(UUID agvId) {
        Agv updatedAgv = agvService.getAgvMap().get(agvId);
        agvLogEntryRepository.save(new AgvLogEntry(
                updatedAgv.getBattery(),
                updatedAgv.getAgvProgram(),
                updatedAgv.getAgvState(),
                updatedAgv
        ));
    }
}