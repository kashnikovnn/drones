package com.musala.drones.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musala.drones.dto.MedicationDto;
import com.musala.drones.service.MedicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(MedicationController.class)
public class MedicationControllerTest {

    @MockBean
    private MedicationService medicationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testSaveMedication() throws Exception {
        // Arrange
        MedicationDto medicationDto = new MedicationDto();
        medicationDto.setName("TestMedication");
        medicationDto.setWeight(10);
        medicationDto.setCode("TESTCODE");

        byte[] imageBytes = new byte[]{1, 2, 3};

        MockMultipartFile medicationPart = new MockMultipartFile(
                "medication",
                "medication.json",
                "application/json",
                objectMapper.writeValueAsBytes(medicationDto)
        );

        MockMultipartFile filePart = new MockMultipartFile(
                "file",
                "image.png",
                "image/png",
                imageBytes
        );

        doNothing().when(medicationService).saveMedication(medicationDto);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.multipart("/medications")
                        .file(medicationPart)
                        .file(filePart))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetMedication() throws Exception {
        // Arrange
        MedicationDto medicationDto = new MedicationDto();
        medicationDto.setId(1);
        medicationDto.setName("TestMedication");
        medicationDto.setWeight(10);
        medicationDto.setCode("TESTCODE");
        medicationDto.setImage(new byte[0]);

        when(medicationService.getMedicationDto(1)).thenReturn(medicationDto);

        // Act and Assert
        mockMvc.perform(get("/medications/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestMedication"))
                .andExpect(jsonPath("$.weight").value(10))
                .andExpect(jsonPath("$.code").value("TESTCODE"))
                .andExpect(jsonPath("$.image").isEmpty());
    }

    @Test
    public void testGetMedicationsByIdsList() throws Exception {

        // arrange
        MedicationDto medication1 = MedicationDto.builder().id(1).name("Med1").build();
        MedicationDto medication2 = MedicationDto.builder().id(2).name("Med2").build();

        List<Integer> ids = Arrays.asList(1, 2);

        List<MedicationDto> medicationDtos = new ArrayList<>();
        medicationDtos.add(medication1);
        medicationDtos.add(medication2);

        when(medicationService.getMedicationDtosByIdsList(anyList())).thenReturn(medicationDtos);

        // act and assert
        mockMvc.perform(get("/medications/list/{ids}", "1,2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(medication1.getId()))
                .andExpect(jsonPath("$[0].name").value(medication1.getName()))
                .andExpect(jsonPath("$[1].id").value(medication2.getId()))
                .andExpect(jsonPath("$[1].name").value(medication2.getName()));
    }
}