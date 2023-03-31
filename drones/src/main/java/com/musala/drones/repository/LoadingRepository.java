package com.musala.drones.repository;

import com.musala.drones.model.Loading;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LoadingRepository extends CrudRepository<Loading, Integer> {

    List<Loading> getLoadingsByDrone_Id(Integer id);
}
