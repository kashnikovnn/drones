package com.musala.drones.repository;

import com.musala.drones.model.Drone;
import com.musala.drones.model.enums.DroneState;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DroneRepository extends CrudRepository<Drone,Integer> {

    List<Drone> getDronesByStateIn(List<DroneState> droneStates);

}
