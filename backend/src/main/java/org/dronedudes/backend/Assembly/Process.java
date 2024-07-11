package org.dronedudes.backend.Assembly;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Process {
    @JsonProperty("ProcessID")
    private int ProcessID;
}
