package org.dronedudes.backend.Blueprint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlueprintCreateRequest {
    private String productTitle;
    private String description;
    private List<Long> partsList;

}
