package com.musala.drones.dto.mappers;

import com.musala.drones.dto.DroneDto;
import com.musala.drones.model.Drone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DroneMapper {

    @Mapping(expression = "java(drone.getModel()==null?null:drone.getModel().getModelName())", target = "model")
    DroneDto mapToDroneDto(Drone drone);

    @Mapping(expression = "java(droneDto.getModel()==null?null:com.musala.drones.model.enums.DroneModel.getByModelName(droneDto.getModel()))", target = "model")
    Drone mapToDrone(DroneDto droneDto);
}
