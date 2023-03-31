package com.musala.drones.model.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum DroneModel {

    LIGHTWEIGHT("Lightweight"),
    MIDDLEWEIGHT("Middleweight"),
    CRUISERWEIGHT("Cruiserweight"),
    HEAVYWEIGHT("Heavyweight");
    private final String modelName;

    DroneModel(String modelName) {
        this.modelName = modelName;
    }

    public static DroneModel getByModelName(String modelName) {
        return Arrays.stream(DroneModel.values())
                .filter(droneModel -> droneModel.getModelName().equals(modelName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Drone model \""
                        + modelName + "\" not found"));
    }
}
