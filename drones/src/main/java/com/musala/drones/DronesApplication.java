package com.musala.drones;

import com.musala.drones.service.DroneService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DronesApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(DronesApplication.class, args);
		DroneService droneService = configurableApplicationContext.getBean(DroneService.class);
		droneService.getDrone(1);
	}
}
