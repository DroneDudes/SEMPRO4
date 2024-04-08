package org.dronedudes.backend.agv;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

//    @ManyToOne()
//    @JoinColumn(name = "agv_program_id")
//    private AgvProgram agv_program;

//    @ManyToOne()
//    @JoinColumn(name = "agv_state_id")
//    private AgvState agv_state;

    @ManyToOne()
    @JoinColumn(name = "agv")
    private Agv agv;

    private int userId = 1;
    private int carriedPartId = 100;

    public AgvLogEntry(int battery, String agvProgram, int agvState, Agv agv) {
        this.battery = battery;
        this.agvProgram = agvProgram;
        this.agvState = agvState;
        this.agv = agv;
    }
}
