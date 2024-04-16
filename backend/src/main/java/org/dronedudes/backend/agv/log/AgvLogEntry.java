package org.dronedudes.backend.agv.log;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dronedudes.backend.agv.Agv;
import org.dronedudes.backend.agv.program.AgvProgramEnum;
import org.dronedudes.backend.agv.state.AgvStateEnum;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    @Column(name = "agv_program", nullable = false)
    private String agvProgram;

    @Column(name = "agv_state", nullable = false)
    private int agvState;

    private String timestamp;

    @ManyToOne()
    @JoinColumn(name = "agv")
    private Agv agv;

    private int userId = 1;
    private int carriedPartId = 100;


    public AgvLogEntry(int battery, AgvProgramEnum agvProgram, AgvStateEnum agvState, Agv agv) {
        this.battery = battery;
        this.agvProgram = agvProgram.getProgramName();
        this.agvState = agvState.getState();
        this.agv = agv;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.timestamp = LocalDateTime.now().format(dateTimeFormatter);
    }
}
