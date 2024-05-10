//package org.dronedudes.backend.Part;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import java.util.Arrays;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.*;
//
//public class ServiceTest {
//
//    private PartRepository partRepository;
//    private PartService partService;
//
//    @BeforeEach
//    public void setup() {
//        partRepository = Mockito.mock(PartRepository.class);
//        partService = new PartService(partRepository);
//    }
//
//    @Test
//    public void testCreatePart() {
//        Part part = new Part("part1", "part1", "part1", "part1", 1L);
//
//        when(partRepository.save(any(Part.class))).thenReturn(part);
//
//        Part createdPart = partService.createPart(part);
//
//        assertEquals(part, createdPart);
//    }
//
//    @Test
//    public void testGetAll() {
//        when(partRepository.findAll()).thenReturn(Arrays.asList(new Part(), new Part()));
//
//        assertEquals(2, partService.getAll().size());
//    }
//
//    @Test
//    public void testGetPartById() {
//        Part part = new Part("part1", "part1", "part1", "part1", 1L);
//
//        when(partRepository.findById(1L)).thenReturn(Optional.of(part));
//
//        Optional<Part> retrievedPart = partService.getPartById(1L);
//
//        assertNotNull(retrievedPart);
//        assertEquals(part, retrievedPart.get());
//    }
//
//    @Test
//    public void testCreatePartFromAngular() {
//        PartDTO partDTO = new PartDTO();
//        partDTO.setName("part1");
//        partDTO.setDescription("part1");
//        partDTO.setSpecifications("part1");
//        partDTO.setSupplierDetails("part1");
//        partDTO.setPrice(1L);
//
//        Part part = new Part("part1", "part1", "part1", "part1", 1L);
//
//        when(partRepository.save(any(Part.class))).thenReturn(part);
//
//        Part createdPart = partService.createPartFromAngular(partDTO);
//
//        assertEquals(part, createdPart);
//    }
//}