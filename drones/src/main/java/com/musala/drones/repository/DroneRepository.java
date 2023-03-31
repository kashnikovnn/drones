package com.musala.drones.repository;

import com.musala.drones.model.Drone;
import org.springframework.data.repository.CrudRepository;

public interface DroneRepository extends CrudRepository<Drone,Integer> {
}
