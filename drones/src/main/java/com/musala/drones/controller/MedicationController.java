package com.musala.drones.controller;

import com.musala.drones.dto.MedicationDto;
import com.musala.drones.service.MedicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/medications")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationService medicationService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> saveMedication(@RequestPart("medication") MedicationDto medicationDto,
                                               @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        if (imageFile != null) {
            try {
                medicationDto.setImage(imageFile.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read image file", e);
            }
        }
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
