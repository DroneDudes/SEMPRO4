package org.dronedudes.backend.agv;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.dronedudes.backend.agv.log.AgvLogEntry;
import org.dronedudes.backend.agv.program.AgvProgramEnum;
import org.dronedudes.backend.agv.state.AgvStateEnum;
import org.dronedudes.backend.common.Machine;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "agv")
public class Agv extends Machine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "endpoint_url", nullable = false)
    private String endpointUrl = "http://localhost:8082/v1/status/";

    @Transient
    private int battery;

    @Transient
    private AgvProgramEnum agvProgram;

    @Transient
    private AgvStateEnum agvState;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "agv", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<AgvLogEntry> agvLogsEntries;
    @Column(nullable = false)
    private String name;

    public Agv(String name, String endpointUrl) {
        this.name = name;
        this.endpointUrl = endpointUrl;
    }
}
