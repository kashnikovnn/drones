package com.musala.drones.service;

import com.musala.drones.dto.MedicationDto;
import com.musala.drones.dto.mappers.MedicationMapper;
import com.musala.drones.model.Medication;
import com.musala.drones.repository.MedicationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class MedicationServiceTest {

    @InjectMocks
    private MedicationService medicationService;

    @Mock
    private MedicationRepository medicationRepository;

    @Mock
    private MedicationMapper medicationMapper;


    @Test
    void shouldSaveMedication() {
        MedicationDto medicationDto = new MedicationDto();
        medicationDto.setName("Medication1");
        medicationDto.setCode("CODE1");

        Medication medication = new Medication();
        medication.setName("Medication1");
        medication.setCode("CODE1");

        when(medicationMapper.toMedication(any())).thenReturn(medication);

        medicationService.saveMedication(medicationDto);
    }

    @Test
    void shouldNotSaveMedicationWithInvalidName() {
        MedicationDto medicationDto = new MedicationDto();
        medicationDto.setName("Medication1$");
        medicationDto.setCode("CODE1");

        assertThrows(RuntimeException.class, () -> medicationService.saveMedication(medicationDto));
    }

    @Test
    void shouldNotSaveMedicationWithInvalidCode() {
        MedicationDto medicationDto = new MedicationDto();
        medicationDto.setName("Medication1");
        medicationDto.setCode("code1");

        assertThrows(RuntimeException.class, () -> medicationService.saveMedication(medicationDto));
    }

    @Test
    void shouldGetMedicationDtosByIdsList() {
        Medication medication1 = new Medication();
        medication1.setId(1);
        medication1.setName("Medication1");
        medication1.setCode("CODE1");

        Medication medication2 = new Medication();
        medication2.setId(2);
        medication2.setName("Medication2");
        medication2.setCode("CODE2");

        MedicationDto medicationDto1 = new MedicationDto();
        medicationDto1.setId(1);
        medicationDto1.setName("Medication1");
        medicationDto1.setCode("CODE1");

        MedicationDto medicationDto2 = new MedicationDto();
        medicationDto2.setId(2);
        medicationDto2.setName("Medication2");
        medicationDto2.setCode("CODE2");

        List<Integer> ids = Arrays.asList(1, 2);
        when(medicationRepository.getMedicationByIdIn(ids)).thenReturn(Arrays.asList(medication1, medication2));
        when(medicationMapper.toMedicationDto(medication1)).thenReturn(medicationDto1);
        when(medicationMapper.toMedicationDto(medication2)).thenReturn(medicationDto2);

        List<MedicationDto> medicationDtos = medicationService.getMedicationDtosByIdsList(ids);

        assertAll(
                () -> assertEquals(2, medicationDtos.size()),
                () -> assertEquals(medicationDto1, medicationDtos.get(0)),
                () -> assertEquals(medicationDto2, medicationDtos.get(1))
        );
    }
}


