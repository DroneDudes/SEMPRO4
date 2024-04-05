package org.dronedudes.backend.agv;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
public class AgvService {
    private final AgvRepository agvRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private Agv agv;


    public Optional<Agv> returnSingleAgv() {
        return agvRepository.findFirstByOrderById();
    }
}
