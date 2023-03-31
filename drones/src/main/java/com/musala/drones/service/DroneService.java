package com.musala.drones.service;


import com.musala.drones.dto.DroneDto;
import com.musala.drones.dto.MedicationDto;
import com.musala.drones.dto.loading.DroneLoadingDto;
import com.musala.drones.dto.loading.MedicationQuantityDto;
import com.musala.drones.dto.mappers.DroneMapper;
import com.musala.drones.model.Drone;
import com.musala.drones.repository.DroneRepository;
import com.musala.drones.repository.LoadingRepository;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DroneService {

    private final DroneRepository droneRepository;

    private final LoadingRepository loadingRepository;
    private final DroneMapper droneMapper;

    private final MedicationService medicationService;

    public void registerDrone(DroneDto droneDto) {
        checkDrone(droneDto);
        Drone drone = droneMapper.mapToDrone(droneDto);
        droneRepository.save(drone);
    }

    public Drone getDrone(Integer id) {
        return droneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Drone with id="+id+" not found"));
    }

    public void loadDrone(DroneLoadingDto droneLoadingDto) {

        List<Integer> medicationIds = droneLoadingDto.getMedications()
                .stream()
                .map(MedicationQuantityDto::getMedicationId)
                .collect(Collectors.toList());

        List<MedicationDto> medications = medicationService.getMedicationsByIsList(medicationIds);

        List<MedicationCount> medicationCounts = createMedicationsCount(medications,droneLoadingDto);

        Drone drone = getDrone(droneLoadingDto.getDroneId());
        checkLoadPossibility(drone,medicationCounts);




    }

    private void checkLoadPossibility(Drone drone, List<MedicationCount> medicationCounts ){
        if (drone.getWeightLimit().intValue() <
                calculateMedicationsWeight(medicationCounts) + calculateDroneCurrentLoadingWeight(drone.getId())){
            throw new RuntimeException("Can't load drone. Overweight.");
        }
    }

    private Integer calculateDroneCurrentLoadingWeight(Integer droneId) {
        return loadingRepository.getLoadingsByDrone_Id(droneId)
                .stream()
                .map(loading -> loading.getMedication().getWeight().intValue() * loading.getQty())
                .reduce(0, Integer::sum);
    }

    private Integer calculateMedicationsWeight(List<MedicationCount> medicationCounts) {
        return medicationCounts.stream()
                .map(medicationCount -> medicationCount.medicationDto.getWeight() * medicationCount.count)
                .reduce(0, Integer::sum);
    }

    private List<MedicationCount> createMedicationsCount(List<MedicationDto> medications, DroneLoadingDto droneLoadingDto){
        return droneLoadingDto.getMedications()
                .stream().map(medicationQuantityDto ->
                        MedicationCount.builder()
                                .medicationDto(
                                        medications.stream()
                                                .filter(medicationDto ->
                                                        medicationDto.getId().equals(medicationQuantityDto.getMedicationId())
                                                )
                                                .findFirst()
                                                .orElseThrow(()-> new RuntimeException("Medication with id="
                                                        +medicationQuantityDto.getMedicationId()+" not found"))
                                )
                                .count(medicationQuantityDto.getQuantity())
                                .build()

                ).collect(Collectors.toList());
    }


    private void checkDrone(DroneDto drone) {
        if (drone.getSerialNumber().length() > 100) {
            throw new IllegalArgumentException("Serial number is too long");
        }

        if (drone.getWeightLimit() > 500 || drone.getWeightLimit() < 0) {
            throw new IllegalArgumentException("Weight limit must be between 0 and 500");
        }

        if (drone.getBatteryCapacity() < 0 || drone.getBatteryCapacity() > 100) {
            throw new IllegalArgumentException("Battery capacity must be between 0 and 100");
        }
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    class MedicationCount {
        private MedicationDto medicationDto;
        private Integer count;
    }
}
