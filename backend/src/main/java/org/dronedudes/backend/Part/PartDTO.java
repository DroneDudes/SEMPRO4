package org.dronedudes.backend.Part;

import lombok.Data;

import java.util.List;

@Data
public class PartDTO {
    private String name;
    private String description;

    private String specifications;

    private String supplierDetails;
    private long price;
}
