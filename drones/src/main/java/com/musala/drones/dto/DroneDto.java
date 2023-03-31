package com.musala.drones.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DroneDto {

    private Integer id;

    private String serialNumber;

    private String model;

    private Integer weightLimit;

    private Integer batteryCapacity;

    private String state;
}
