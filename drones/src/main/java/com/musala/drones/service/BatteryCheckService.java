package com.musala.drones.service;

import com.musala.drones.model.Drone;
import com.musala.drones.model.BatteryLevelLog;

import java.util.List;

public interface BatteryCheckService {

    List<BatteryLevelLog> checkBatteryLevels(List<Drone> drones);
}
