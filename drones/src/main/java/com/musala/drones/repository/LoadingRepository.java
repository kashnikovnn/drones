package com.musala.drones.repository;

import com.musala.drones.model.Loading;
import com.musala.drones.model.LoadingPK;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LoadingRepository extends CrudRepository<Loading, LoadingPK> {

    List<Loading> getLoadingsByLoadingPK_DroneId(Integer id);


}
