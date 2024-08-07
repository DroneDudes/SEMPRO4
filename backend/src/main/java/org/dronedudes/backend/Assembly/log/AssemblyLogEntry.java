package org.dronedudes.backend.Assembly.log;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dronedudes.backend.Assembly.AssemblyStation;
import org.dronedudes.backend.agv.Agv;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "assembly_log_entry")
public class AssemblyLogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assembly_state")
    private int state;

    @Column(name = "assembly_operation")
    private int operationId;

    private String timestamp;

    @ManyToOne()
    @JoinColumn(name = "assembly_station")
    private AssemblyStation assemblyStation;


    public AssemblyLogEntry(int operationId, int state, AssemblyStation assemblyStation) {
        this.operationId = operationId;
        this.state = state;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.timestamp = LocalDateTime.now().format(dateTimeFormatter);
        this.assemblyStation = assemblyStation;
    }
}
