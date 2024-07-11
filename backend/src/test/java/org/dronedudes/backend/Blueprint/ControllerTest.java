package org.dronedudes.backend.Blueprint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BlueprintController.class)
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BlueprintService blueprintService;

    private Blueprint blueprint;

    @BeforeEach
    public void setup() {
        blueprint = new Blueprint();
        // Set the properties of the blueprint object
    }

    @Test
    public void testGetAllBlueprints() throws Exception {
        when(blueprintService.getAll()).thenReturn(Arrays.asList(blueprint));

        mockMvc.perform(get("/api/v1/blueprints/all"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetBlueprintById() throws Exception {
        when(blueprintService.getById(1L)).thenReturn(blueprint);

        mockMvc.perform(get("/api/v1/blueprints/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetBlueprintsByPartId() throws Exception {
        when(blueprintService.getBlueprintsByPartId(1L)).thenReturn(Arrays.asList(blueprint));

        mockMvc.perform(get("/api/v1/blueprints/part/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testNewBlueprint() throws Exception {
        BlueprintCreateRequest createRequest = new BlueprintCreateRequest();

        when(blueprintService.createAndSaveBlueprint(any(BlueprintCreateRequest.class))).thenReturn(blueprint);

        mockMvc.perform(post("/api/v1/blueprints/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk());
    }
}