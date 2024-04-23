package org.dronedudes.backend.Blueprint;

import org.dronedudes.backend.Part.Part;
import org.dronedudes.backend.Part.PartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ServiceTest {

    private BlueprintRepository blueprintRepository;
    private PartRepository partRepository;
    private BlueprintService blueprintService;

    @BeforeEach
    public void setup() {
        blueprintRepository = Mockito.mock(BlueprintRepository.class);
        partRepository = Mockito.mock(PartRepository.class);
        blueprintService = new BlueprintService(blueprintRepository, partRepository);
    }

    @Test
    public void testCreateAndSaveBlueprint() {
        BlueprintCreateRequest createRequest = new BlueprintCreateRequest();
        createRequest.setProductTitle("Test Product");
        createRequest.setDescription("Test Description");
        createRequest.setPartsList(Arrays.asList(1L, 2L));

        Part part1 = new Part("part1", "part1", "part1", "part1", 1L);
        Part part2 = new Part("part2", "part2", "part2", "part2", 2L);

        when(partRepository.findById(1L)).thenReturn(Optional.of(part1));
        when(partRepository.findById(2L)).thenReturn(Optional.of(part2));

        Blueprint blueprint = blueprintService.createAndSaveBlueprint(createRequest);

        assertEquals("Test Product", blueprint.getProductTitle());
        assertEquals("Test Description", blueprint.getDescription());
        assertEquals(2, blueprint.getParts().size());
    }

    @Test
    public void testGetAll() {
        when(blueprintRepository.findAll()).thenReturn(Arrays.asList(new Blueprint(), new Blueprint()));

        assertEquals(2, blueprintService.getAll().size());
    }

    @Test
    public void testGetById() {
        when(blueprintRepository.findById(1L)).thenReturn(Optional.of(new Blueprint()));

        Blueprint blueprint = blueprintService.getById(1L);

        assertNotNull(blueprint);
    }

    @Test
    public void testGetBlueprintsByPartId() {
        when(blueprintRepository.findBlueprintsByPartsId(1L)).thenReturn(Arrays.asList(new Blueprint(), new Blueprint()));

        assertEquals(2, blueprintService.getBlueprintsByPartId(1L).size());
    }
}