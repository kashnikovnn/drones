package com.musala.drones.service;


import com.musala.drones.dto.DroneDto;
import com.musala.drones.dto.loading.DroneLoadingRequestDto;
import com.musala.drones.dto.loading.DroneLoadingResponseDto;
import com.musala.drones.dto.loading.MedicationQuantityRequestDto;
import com.musala.drones.dto.loading.MedicationQuantityResponseDto;
import com.musala.drones.dto.mappers.DroneMapper;
import com.musala.drones.dto.mappers.MedicationMapper;
import com.musala.drones.model.Drone;
import com.musala.drones.model.Loading;
import com.musala.drones.model.LoadingPK;
import com.musala.drones.model.Medication;
import com.musala.drones.model.enums.DroneState;
import com.musala.drones.repository.DroneRepository;
import com.musala.drones.repository.LoadingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DroneService {

    private final DroneRepository droneRepository;
    private final LoadingRepository loadingRepository;
    private final DroneMapper droneMapper;
    private final MedicationService medicationService;
    private final MedicationMapper medicationMapper;


    public void registerDrone(DroneDto droneDto) {
        checkDroneDto(droneDto);
        Drone drone = droneMapper.mapToDrone(droneDto);
        droneRepository.save(drone);
    }

    public Drone getDrone(Integer id) {
        return droneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Drone with id=" + id + " not found"));
    }

    public DroneDto getDroneDto(Integer id) {
        return droneMapper.mapToDroneDto(getDrone(id));
    }

    public List<DroneDto> getLoadingAvailableDrones() {
        return droneRepository.getDronesByStateIn(List.of(DroneState.IDLE, DroneState.LOADING))
                .stream()
                .map(droneMapper::mapToDroneDto)
                .collect(Collectors.toList());
    }

    public Integer getBatteryLevel(Integer droneId) {
        return getDrone(droneId).getBatteryCapacity().intValue();
    }

    @Transactional
    public void loadDrone(DroneLoadingRequestDto droneLoadingRequestDto) {

        List<Integer> medicationIds = droneLoadingRequestDto.getMedications()
                .stream()
                .map(MedicationQuantityRequestDto::getMedicationId)
                .collect(Collectors.toList());

        List<Medication> medications = medicationService.getMedicationsByIdsList(medicationIds);

        Drone drone = getDrone(droneLoadingRequestDto.getDroneId());

        List<Loading> newLoadings = createLoadings(medications, drone, droneLoadingRequestDto);
        List<Loading> currentDroneLoadings = loadingRepository.getLoadingsByLoadingPK_DroneId(drone.getId());

        checkLoadPossibility(drone, newLoadings, currentDroneLoadings);

        mergeLoadingsQty(newLoadings, currentDroneLoadings);

        drone.setState(DroneState.LOADING);
        droneRepository.save(drone);
        medicationService.saveMedications(medications);

    }

    private void mergeLoadingsQty(List<Loading> newLoadings, List<Loading> currentDroneLoadings) {
        newLoadings.forEach(newLoading -> {
            currentDroneLoadings.stream()
                    .filter(cl -> cl.getLoadingPK().equals(newLoading.getLoadingPK()))
                    .findFirst().ifPresent(currentLoading -> newLoading.setQty(newLoading.getQty() + currentLoading.getQty()));
        });
    }

    public DroneLoadingResponseDto getDroneLoading(Integer droneId) {
        DroneLoadingResponseDto droneLoadingResponseDto = new DroneLoadingResponseDto();
        List<Loading> loadings = loadingRepository.getLoadingsByLoadingPK_DroneId(droneId);

        droneLoadingResponseDto.setDrone(getDroneDto(droneId));
        droneLoadingResponseDto.setMedications(
                loadings.stream()
                        .map(loading -> MedicationQuantityResponseDto.builder()
                                .medication(medicationMapper.toMedicationDto(loading.getLoadingPK().getMedication()))
                                .quantity(loading.getQty())
                                .build()
                        ).collect(Collectors.toList())
        );
        droneLoadingResponseDto.setSummaryWeight(calculateMedicationsWeight(loadings));

        return droneLoadingResponseDto;
    }

    private void checkLoadPossibility(Drone drone, List<Loading> newLoadings, List<Loading> currentDroneLoadings) {

        if (!(DroneState.IDLE.equals(drone.getState())
                || DroneState.LOADING.equals(drone.getState()))
        ) {
            throw new RuntimeException("Can't load. Wrong drone state:" + drone.getState());
        }

        if(drone.getBatteryCapacity().intValue() < 25){
            throw new RuntimeException("Can't load drone. Battery level is below 25%.");
        }

        if (drone.getWeightLimit().intValue() <
                calculateMedicationsWeight(newLoadings) + calculateMedicationsWeight(currentDroneLoadings)) {
            throw new RuntimeException("Can't load drone. Overweight.");
        }
    }


    private Integer calculateMedicationsWeight(List<Loading> loadings) {
        return loadings.stream()
                .map(loading -> loading.getLoadingPK().getMedication().getWeight() * loading.getQty())
                .reduce(0, Integer::sum);
    }

    private List<Loading> createLoadings(List<Medication> medications, Drone drone, DroneLoadingRequestDto droneLoadingRequestDto) {
        return droneLoadingRequestDto.getMedications()
                .stream().map(medicationQuantityRequestDto -> {
                            LoadingPK loadingPK = new LoadingPK();
                            loadingPK.setDrone(drone);
                            loadingPK.setMedication(medications.stream()
                                    .filter(medication ->
                                            medication.getId().equals(medicationQuantityRequestDto.getMedicationId())
                                    )
                                    .findFirst()
                                    .orElseThrow(() -> new RuntimeException("Medication with id="
                                            + medicationQuantityRequestDto.getMedicationId() + " not found"))
                            );
                            return Loading.builder()
                                    .loadingPK(loadingPK)
                                    .qty(medicationQuantityRequestDto.getQuantity())
                                    .build();
                        }
                ).collect(Collectors.toList());
    }

    private void checkDroneDto(DroneDto drone) {
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


}
