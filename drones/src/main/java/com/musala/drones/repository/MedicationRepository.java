package com.musala.drones.repository;

import com.musala.drones.model.Medication;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MedicationRepository extends CrudRepository<Medication,Integer> {

    List<Medication> getMedicationByIdIn(List<Integer> id);
}
