package org.dronedudes.backend.agv.log;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dronedudes.backend.agv.Agv;
import org.dronedudes.backend.agv.program.AgvProgramEnum;
import org.dronedudes.backend.agv.state.AgvStateEnum;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "agv_log_entry")
public class AgvLogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int battery;

    @Enumerated(EnumType.STRING)
    @Column(name = "agv_program", nullable = false)
    private AgvProgramEnum agvProgram;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "agv_state", nullable = false)
    private AgvStateEnum agvState;


    @ManyToOne()
    @JoinColumn(name = "agv")
    private Agv agv;

    private int userId = 1;
    private int carriedPartId = 100;

    public AgvLogEntry(int battery, AgvProgramEnum agvProgram, AgvStateEnum agvState, Agv agv) {
        this.battery = battery;
        this.agvProgram = agvProgram;
        this.agvState = agvState;
        this.agv = agv;
    }
}
