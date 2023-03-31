package com.musala.drones.dto.loading;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DroneLoadingDto {

    private Integer droneId;

    private List<MedicationQuantityDto> medications;

}
