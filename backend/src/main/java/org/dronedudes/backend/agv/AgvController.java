package org.dronedudes.backend.agv;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/agv")
@AllArgsConstructor
public class AgvController {
    private final AgvService agvService;

    @GetMapping("/")
    public ResponseEntity<Optional<Agv>> getAgv() {
        return new ResponseEntity<>(agvService.returnSingleAgv(), HttpStatus.OK);
    }

}
