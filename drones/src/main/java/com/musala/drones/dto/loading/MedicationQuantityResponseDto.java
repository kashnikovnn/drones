package com.musala.drones.dto.loading;

import com.musala.drones.dto.MedicationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicationQuantityResponseDto {

    private MedicationDto medication;

    private Integer quantity;
}
