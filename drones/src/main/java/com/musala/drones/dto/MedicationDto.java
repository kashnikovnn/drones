package com.musala.drones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicationDto {

    private Integer id;
    private String name;
    private Integer weight;
    private String code;
    private byte[] image;

}
