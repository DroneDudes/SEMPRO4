package org.dronedudes.backend.Part;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Configuration
public class PartConfiguration {

    private static final Logger logger = Logger.getLogger(PartConfiguration.class.getName());
    private final PartService partService;

    public PartConfiguration(PartService partService) {
        this.partService = partService;
    }

    @Bean(name = "createStandardPart")
    public List<Part> createStandardPart() {
        logger.info("Creating standard parts...");
        List<Part> parts = new ArrayList<>();
        Part standardDroneParts = new Part("Standard Drone Parts", "Kit of standard drone parts", "Standard drone parts", "DJI Ltd.", 899);
        Part highEndDroneParts = new Part("High-end Drone Parts", "Kit of high-end drone parts", "High-end drone parts", "DJI Ltd.", 1999);
        Part thermalCamera = new Part("Thermal Camera", "Thermal camera for drones", "Thermal camera", "Sony Inc.", 499);
        Part gpsModule = new Part("GPS Module", "GPS module for drones", "GPS module", "Garmin Ltd.", 99);
        parts.add(standardDroneParts);
        parts.add(highEndDroneParts);
        parts.add(thermalCamera);
        parts.add(gpsModule);

        for(Part part : parts) {
            partService.createPart(part);
        }
        logger.info("Standard parts created successfully.");
        return parts;
    }
}
