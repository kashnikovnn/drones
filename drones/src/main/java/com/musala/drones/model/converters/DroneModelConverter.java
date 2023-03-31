package com.musala.drones.model.converters;

import com.musala.drones.model.enums.DroneModel;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class DroneModelConverter implements AttributeConverter<DroneModel,String> {

    @Override
    public String convertToDatabaseColumn(DroneModel droneModel) {
        return droneModel.getModelName();
    }

    @Override
    public DroneModel convertToEntityAttribute(String s) {
        return DroneModel.getByModelName(s);
    }
}
