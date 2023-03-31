package com.musala.drones.dto.mappers;

import com.musala.drones.dto.DroneDto;
import com.musala.drones.model.Drone;
import com.musala.drones.model.enums.DroneModel;
import com.musala.drones.model.enums.DroneState;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class DroneMapperTest {

    DroneMapper droneMapper = Mappers.getMapper(DroneMapper.class);

    @Test
    void mapToDroneDto() {
        Drone drone = Drone.builder()
                .id(1)
                .batteryCapacity(Byte.valueOf("99"))
                .serialNumber("SERIAL123")
                .model(DroneModel.LIGHTWEIGHT)
                .state(DroneState.IDLE)
                .weightLimit(Short.valueOf("230"))
                .build();
        DroneDto droneDto = droneMapper.mapToDroneDto(drone);
        assertEquals(droneDto.getModel(),DroneModel.LIGHTWEIGHT.getModelName());
        assertEquals(droneDto.getState(),DroneState.IDLE.name());
        assertEquals(droneDto.getBatteryCapacity(),drone.getBatteryCapacity().intValue());
    }

    @Test
    void mapToDrone() {
        DroneDto droneDto = DroneDto.builder()
                .batteryCapacity(99)
                .serialNumber("SERIAL123")
                .model(DroneModel.LIGHTWEIGHT.getModelName())
                .state(DroneState.IDLE.name())
                .weightLimit(230)
                .build();
        Drone drone = droneMapper.mapToDrone(droneDto);
        assertEquals(drone.getModel(),DroneModel.LIGHTWEIGHT);
        assertEquals(drone.getState(),DroneState.IDLE);
        assertEquals(drone.getBatteryCapacity(),Byte.valueOf("99"));
    }
}