package org.dronedudes.backend.Blueprint;

import org.dronedudes.backend.Part.Part;
import org.dronedudes.backend.Part.PartService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Configuration
@DependsOn("createStandardPart")
public class BlueprintConfiguration {

    private static final Logger logger = Logger.getLogger(BlueprintConfiguration.class.getName());
    private final BlueprintService blueprintService;
    private final PartService partService;

    public BlueprintConfiguration(BlueprintService blueprintService, PartService partService) {
        this.blueprintService = blueprintService;
        this.partService = partService;
    }

    @Bean
    public List<BlueprintCreateRequest> createStandardBlueprints(){
        logger.info("Creating standard blueprints...");
        List<BlueprintCreateRequest> blueprintCreateRequests = new ArrayList<>();
        List<Part> parts = partService.getAll();

        if (parts.size() < 4) {
            logger.severe("Not enough parts available to create standard blueprints");
            throw new IllegalStateException("Not enough parts available to create standard blueprints");
        }

        List<Long> standardDroneParts = new ArrayList<>();
        standardDroneParts.add(parts.get(0).getId()); // Ensure that parts list is not empty and contains valid IDs
        BlueprintCreateRequest standardDrone = new BlueprintCreateRequest("Standard Drone", "Standard drone", standardDroneParts);
        blueprintCreateRequests.add(standardDrone);

        List<Long> highEndDroneParts = new ArrayList<>();
        highEndDroneParts.add(parts.get(1).getId());
        BlueprintCreateRequest highEndDrone = new BlueprintCreateRequest("High-end Drone", "High-end drone", highEndDroneParts);
        blueprintCreateRequests.add(highEndDrone);

        List<Long> thermalCameraParts = new ArrayList<>();
        thermalCameraParts.add(parts.get(0).getId());
        thermalCameraParts.add(parts.get(2).getId());
        BlueprintCreateRequest thermalCameraDrone = new BlueprintCreateRequest("Thermal Camera Drone", "Thermal camera drone", thermalCameraParts);
        blueprintCreateRequests.add(thermalCameraDrone);

        List<Long> gpsModuleParts = new ArrayList<>();
        gpsModuleParts.add(parts.get(1).getId());
        gpsModuleParts.add(parts.get(3).getId());
        BlueprintCreateRequest gpsModuleDrone = new BlueprintCreateRequest("GPS Module Drone", "GPS module drone", gpsModuleParts);
        blueprintCreateRequests.add(gpsModuleDrone);

        for (BlueprintCreateRequest blueprintCreateRequest : blueprintCreateRequests) {
            blueprintService.createAndSaveBlueprint(blueprintCreateRequest);
        }

        logger.info("Standard blueprints created successfully.");
        return blueprintCreateRequests;
    }
}
