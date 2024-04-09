package org.dronedudes.backend.agv;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.dronedudes.backend.agv.log.AgvLogEntry;
import org.dronedudes.backend.agv.program.AgvProgramEnum;
import org.dronedudes.backend.agv.state.AgvStateEnum;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "agv")
public class Agv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public Agv(String name) {
        this.name = name;
    }
}
