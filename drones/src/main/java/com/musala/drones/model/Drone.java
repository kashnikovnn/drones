package com.musala.drones.model;

import com.musala.drones.model.converters.DroneModelConverter;
import com.musala.drones.model.enums.DroneModel;
import com.musala.drones.model.enums.DroneState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DRONES")
@Data
@Builder
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String serialNumber;


    @Convert(converter = DroneModelConverter.class)
    private DroneModel model;

    private Short weightLimit;

    private Byte batteryCapacity;

    @Enumerated(EnumType.STRING)
    private DroneState state;











}
