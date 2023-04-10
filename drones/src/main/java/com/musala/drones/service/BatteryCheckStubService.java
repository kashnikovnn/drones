package com.musala.drones.service;

import com.musala.drones.model.Drone;
import com.musala.drones.model.BatteryLevelLog;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class BatteryCheckStubService implements BatteryCheckService{

    private static final Random random = new Random();
    @Override
    public List<BatteryLevelLog> checkBatteryLevels(List<Drone> drones) {
        return drones.stream()
                .map(drone -> BatteryLevelLog.builder()
                        .batteryLevel((byte)random.nextInt(101))
                        .droneId(drone.getId())
                        .date(LocalDateTime.now())
                        .build()
                )
        .collect(Collectors.toList());
    }
}
