package com.musala.drones.service;

import com.musala.drones.dto.MedicationDto;
import com.musala.drones.dto.mappers.MedicationMapper;
import com.musala.drones.model.Medication;
import com.musala.drones.repository.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicationService {

    private final MedicationRepository medicationRepository;

    private final MedicationMapper medicationMapper;

    private void checkMedication(MedicationDto medicationDto) {

        if(!medicationDto.getName().matches("^[A-Za-z0-9_-]+$")){
            throw new RuntimeException("In medication name allowed only letters, numbers, ‘-‘, ‘_’");
        }


        if(!medicationDto.getCode().matches("^[A-Z0-9_]+$")){
            throw new RuntimeException("In medication code allowed only upper case letters, underscore and numbers");
        }

    }

    public void saveMedication(MedicationDto medicationDto){
        checkMedication(medicationDto);
        Medication medication = medicationMapper.toMedication(medicationDto);
        medicationRepository.save(medication);
    }

    public List<MedicationDto> getMedicationDtosByIdsList(List<Integer> ids) {

        return getMedicationsByIdsList(ids)
                .stream()
                .map(medicationMapper::toMedicationDto)
                .collect(Collectors.toList());
    }

    public List<Medication> getMedicationsByIdsList(List<Integer> ids) {
        return medicationRepository.getMedicationByIdIn(ids);
    }

    public void saveMedications(List<Medication> medications) {
        medicationRepository.saveAll(medications);
    }

    public MedicationDto getMedicationDto(Integer id){
        return medicationMapper.toMedicationDto(medicationRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Medication with id="+id+" not found."))
        );
    }
}
