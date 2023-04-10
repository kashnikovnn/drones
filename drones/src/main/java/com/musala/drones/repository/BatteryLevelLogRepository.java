package com.musala.drones.repository;

import com.musala.drones.model.BatteryLevelLog;
import org.springframework.data.repository.CrudRepository;

public interface BatteryLevelLogRepository extends CrudRepository<BatteryLevelLog,Long> {
}
