package com.musala.drones.dto.loading;

import com.musala.drones.dto.DroneDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class DroneLoadingResponseDto {

    private DroneDto drone;

    private List<MedicationQuantityResponseDto> medications;

    private Integer summaryWeight;
}
