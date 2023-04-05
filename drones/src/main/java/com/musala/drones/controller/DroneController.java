package com.musala.drones.controller;

import com.musala.drones.dto.DroneDto;
import com.musala.drones.dto.loading.DroneLoadingRequestDto;
import com.musala.drones.dto.loading.DroneLoadingResponseDto;
import com.musala.drones.service.DroneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drones")
@RequiredArgsConstructor
public class DroneController {

    private final DroneService droneService;

    @PostMapping
    public ResponseEntity<Void> registerDrone(@RequestBody DroneDto droneDto) {
        droneService.registerDrone(droneDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DroneDto> getDrone(@PathVariable Integer id) {
        return ResponseEntity.ok(droneService.getDroneDto(id));
    }

    @GetMapping("/loading-available")
    public ResponseEntity<List<DroneDto>> getLoadingAvailableDrones() {
        return ResponseEntity.ok(droneService.getLoadingAvailableDrones());
    }

    @GetMapping("/{id}/battery-level")
    public ResponseEntity<Integer> getBatteryLevel(@PathVariable Integer id) {
        return ResponseEntity.ok(droneService.getBatteryLevel(id));
    }

    @PostMapping("/loading")
    public ResponseEntity<Void> loadDrone(@RequestBody DroneLoadingRequestDto droneLoadingRequestDto) {
        droneService.loadDrone(droneLoadingRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/loading")
    public ResponseEntity<DroneLoadingResponseDto> getDroneLoading(@PathVariable Integer id) {
        return ResponseEntity.ok(droneService.getDroneLoading(id));
    }


}

