package org.dronedudes.backend.Assembly.log;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AssemblyLog {

    @Id
    private int operationId;

    private int state;

    private LocalDateTime timeStamp;

}
