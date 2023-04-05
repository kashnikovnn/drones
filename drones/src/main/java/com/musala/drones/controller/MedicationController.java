package com.musala.drones.controller;

import com.musala.drones.dto.MedicationDto;
import com.musala.drones.service.MedicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medications")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationService medicationService;

    @PostMapping
    public ResponseEntity<Void> saveMedication(@RequestBody MedicationDto medicationDto) {
        medicationService.saveMedication(medicationDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicationDto> getMedication(@PathVariable Integer id) {
        MedicationDto medicationDto = medicationService.getMedicationDto(id);
        return ResponseEntity.ok(medicationDto);
    }

    @GetMapping("/list/{ids}")
    public ResponseEntity<List<MedicationDto>> getMedicationsByIdsList(@PathVariable List<Integer> ids) {
        List<MedicationDto> medicationDtos = medicationService.getMedicationDtosByIdsList(ids);
        return ResponseEntity.ok(medicationDtos);
    }

}
