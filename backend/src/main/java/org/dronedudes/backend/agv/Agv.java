package org.dronedudes.backend.agv;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "agv")
@Getter
public class Agv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int battery;

    @Column(name = "program_name")
    private String programName;

    private int state;

    public Agv(int battery, String programName, int state) {
        this.battery = battery;
        this.programName = programName;
        this.state = state;
    }
}
