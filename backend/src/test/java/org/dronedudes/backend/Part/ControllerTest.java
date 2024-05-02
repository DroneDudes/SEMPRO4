package org.dronedudes.backend.Part;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PartController.class)
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PartService partService;

    private Part part;

    @BeforeEach
    public void setup() {
        part = new Part("part1", "part1", "part1", "part1", 1L);
    }

    @Test
    public void testGetAllParts() throws Exception {
        when(partService.getAll()).thenReturn(Arrays.asList(part));

        mockMvc.perform(get("/api/v1/parts"))
                .andExpect(status().isOk());
    }

    @Test
    public void testNewPart() throws Exception {
        when(partService.createPart(any(Part.class))).thenReturn(part);

        mockMvc.perform(post("/api/v1/parts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(part)))
                .andExpect(status().isOk());
    }

    @Test
    public void testNewPartFromAngular() throws Exception {
        PartDTO partDTO = new PartDTO();
        partDTO.setName("part1");
        partDTO.setDescription("part1");
        partDTO.setSpecifications("part1");
        partDTO.setSupplierDetails("part1");
        partDTO.setPrice(1L);

        when(partService.createPartFromAngular(any(PartDTO.class))).thenReturn(part);

        mockMvc.perform(post("/api/v1/parts/createFromAngular")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPart() throws Exception {
        when(partService.getPartById(1L)).thenReturn(Optional.of(part));

        mockMvc.perform(get("/api/v1/parts/1"))
                .andExpect(status().isOk());
    }
}