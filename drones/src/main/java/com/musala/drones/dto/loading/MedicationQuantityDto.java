package com.musala.drones.dto.loading;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicationQuantityDto {

    private Integer medicationId;

    private Integer quantity;
}
