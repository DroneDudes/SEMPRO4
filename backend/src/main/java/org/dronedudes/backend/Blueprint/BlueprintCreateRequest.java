package org.dronedudes.backend.Blueprint;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class BlueprintCreateRequest {
    private String productTitle;
    private String description;
    private List<Long> partsList;

}
