package org.dronedudes.backend.agv.log;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dronedudes.backend.agv.Agv;
import org.dronedudes.backend.agv.program.AgvProgram;
import org.dronedudes.backend.agv.state.AgvState;

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

    @ManyToOne()
    @JoinColumn(name = "agv_program_id")
    private AgvProgram agv_program;

    @ManyToOne()
    @JoinColumn(name = "agv_state_id")
    private AgvState agv_state;

    @ManyToOne()
    @JoinColumn(name = "agv")
    private Agv agv;

    private int userId = 1;
    private int carriedPartId = 100;
}
