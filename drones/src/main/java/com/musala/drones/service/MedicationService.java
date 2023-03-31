package com.musala.drones.service;

import com.musala.drones.dto.MedicationDto;
import com.musala.drones.dto.mappers.MedicationMapper;
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

    private void checkMedication(){

    }

    public List<MedicationDto> getMedicationsByIsList(List<Integer> ids){

        return medicationRepository.getMedicationByIdIn(ids)
                .stream()
                .map(medicationMapper::toMedicationDto)
                .collect(Collectors.toList());

    }
}
