package com.musala.drones.dto.mappers;

import com.musala.drones.dto.MedicationDto;
import com.musala.drones.model.Medication;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicationMapper {

    Medication toMedication(MedicationDto medicationDto);

    MedicationDto toMedicationDto(Medication medication);

}
