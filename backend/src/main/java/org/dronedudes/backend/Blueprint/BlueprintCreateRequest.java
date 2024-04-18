package org.dronedudes.backend.Blueprint;

import lombok.Data;

import java.util.List;
@Data
public class BlueprintCreateRequest {
    private String productTitle;
    private String description;
    private List<Long> partsList;

}
