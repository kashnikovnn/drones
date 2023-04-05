package com.musala.drones.service;

import com.musala.drones.dto.DroneDto;
import com.musala.drones.dto.loading.DroneLoadingRequestDto;
import com.musala.drones.dto.loading.DroneLoadingResponseDto;
import com.musala.drones.dto.loading.MedicationQuantityRequestDto;
import com.musala.drones.dto.mappers.DroneMapper;
import com.musala.drones.dto.mappers.MedicationMapper;
import com.musala.drones.model.Drone;
import com.musala.drones.model.Loading;
import com.musala.drones.model.LoadingPK;
import com.musala.drones.model.Medication;
import com.musala.drones.model.enums.DroneState;
import com.musala.drones.repository.DroneRepository;
import com.musala.drones.repository.LoadingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class DroneServiceTest {

    @InjectMocks
    private DroneService droneService;

    @Mock
    private DroneRepository droneRepository;

    @Mock
    private LoadingRepository loadingRepository;

    @Spy
    private DroneMapper droneMapper = Mappers.getMapper(DroneMapper.class);

    @Mock
    private MedicationService medicationService;



    @Test
    void testRegisterDrone() {
        DroneDto droneDto = new DroneDto();
        droneDto.setSerialNumber("12345");
        droneDto.setWeightLimit(100);
        droneDto.setBatteryCapacity(50);

        Drone drone = new Drone();
        drone.setSerialNumber("12345");
        drone.setWeightLimit(Short.valueOf("100"));
        drone.setBatteryCapacity(Byte.valueOf("50"));

        when(droneMapper.mapToDrone(droneDto)).thenReturn(drone);
        when(droneRepository.save(drone)).thenReturn(drone);

        assertDoesNotThrow(() -> droneService.registerDrone(droneDto));
        verify(droneRepository, times(1)).save(drone);
    }

    @Test
    void testGetDrone() {
        int droneId = 1;
        Drone drone = new Drone();
        drone.setId(droneId);

        when(droneRepository.findById(droneId)).thenReturn(Optional.of(drone));

        assertEquals(drone, droneService.getDrone(droneId));
    }

    @Test
    void testGetDroneDto() {
        int droneId = 1;
        Drone drone = new Drone();
        drone.setId(droneId);

        DroneDto droneDto = new DroneDto();
        droneDto.setId(droneId);

        when(droneRepository.findById(droneId)).thenReturn(Optional.of(drone));
        when(droneMapper.mapToDroneDto(drone)).thenReturn(droneDto);

        assertEquals(droneDto, droneService.getDroneDto(droneId));
    }


    @Test
    public void testGetLoadingAvailableDrones() {
        // given
        List<Drone> drones = new ArrayList<>();
        Drone drone1 = new Drone();
        drone1.setId(1);
        drone1.setSerialNumber("SN001");
        drone1.setWeightLimit(Short.parseShort("200"));
        drone1.setBatteryCapacity(Byte.parseByte("90"));
        drone1.setState(DroneState.IDLE);
        drones.add(drone1);
        Drone drone2 = new Drone();
        drone2.setId(2);
        drone2.setSerialNumber("SN002");
        drone2.setWeightLimit(Short.parseShort("250"));
        drone2.setBatteryCapacity(Byte.parseByte("80"));
        drone2.setState(DroneState.LOADING);
        drones.add(drone2);
        when(droneRepository.getDronesByStateIn(List.of(DroneState.IDLE, DroneState.LOADING)))
                .thenReturn(drones);

        // when
        List<DroneDto> droneDtos = droneService.getLoadingAvailableDrones();

        // then
        assertEquals(drones.size(), droneDtos.size());
        assertEquals(drones.stream().map(Drone::getId).collect(Collectors.toList()),
                droneDtos.stream().map(DroneDto::getId).collect(Collectors.toList()));
    }

    @Test
    void shouldReturnOnlyIdleAndLoadingDrones() {
        // given
        Drone idleDrone = new Drone();
        idleDrone.setState(DroneState.IDLE);

        Drone loadingDrone = new Drone();
        loadingDrone.setState(DroneState.LOADING);


        when(droneRepository.getDronesByStateIn(Arrays.asList(DroneState.IDLE, DroneState.LOADING)))
                .thenReturn(Arrays.asList(idleDrone, loadingDrone));

        // when
        List<DroneDto> availableDrones = droneService.getLoadingAvailableDrones();

        // then
        assertEquals(2, availableDrones.size());
        assertEquals(idleDrone.getId(), availableDrones.get(0).getId());
        assertEquals(DroneState.IDLE.name(), availableDrones.get(0).getState());
        assertEquals(loadingDrone.getId(), availableDrones.get(1).getId());
        assertEquals(DroneState.LOADING.name(), availableDrones.get(1).getState());
    }

    @Test
    @DisplayName("Load drone with valid parameters")
    public void testLoadDroneWithValidParams() {
        // Arrange
        DroneLoadingRequestDto requestDto = new DroneLoadingRequestDto();
        requestDto.setDroneId(1);
        MedicationQuantityRequestDto medicationQuantityRequestDto = new MedicationQuantityRequestDto();
        medicationQuantityRequestDto.setMedicationId(2);
        medicationQuantityRequestDto.setQuantity(3);
        requestDto.setMedications(Collections.singletonList(medicationQuantityRequestDto));
        Drone drone = new Drone();
        drone.setId(1);
        drone.setWeightLimit((short) 500);
        drone.setState(DroneState.IDLE);
        Medication medication = new Medication();
        medication.setId(2);
        medication.setWeight((short) 50);
        List<Medication> medications = Collections.singletonList(medication);
        Loading loading = new Loading();
        LoadingPK loadingPk = new LoadingPK();
        loadingPk.setDrone(drone);
        loadingPk.setMedication(medication);
        loading.setLoadingPK(loadingPk);
        loading.setQty(3);
        when(droneRepository.findById(1)).thenReturn(java.util.Optional.of(drone));
        when(medicationService.getMedicationsByIdsList(any())).thenReturn(medications);
        when(loadingRepository.getLoadingsByLoadingPK_DroneId(1)).thenReturn(Collections.singletonList(loading));

        // Act
        droneService.loadDrone(requestDto);

        // Assert
        verify(droneRepository, times(1)).save(any());
        verify(medicationService, times(1)).saveMedications(medications);
    }

    @Test
    @DisplayName("Load drone with invalid state")
    public void testLoadDroneWithInvalidState() {
        // Arrange
        DroneLoadingRequestDto requestDto = new DroneLoadingRequestDto();
        requestDto.setDroneId(1);
        requestDto.setMedications(Collections.emptyList());
        Drone drone = new Drone();
        drone.setId(1);
        drone.setState(DroneState.DELIVERED);
        when(droneRepository.findById(1)).thenReturn(java.util.Optional.of(drone));

        // Act + Assert
        assertThrows(RuntimeException.class, () -> droneService.loadDrone(requestDto));
    }

    @Test
    public void testLoadDroneOverweight() {
        // arrange
        Drone drone = new Drone();
        drone.setId(1);
        drone.setWeightLimit((short) 50);
        drone.setState(DroneState.IDLE);

        DroneLoadingRequestDto loadingRequestDto = new DroneLoadingRequestDto();
        loadingRequestDto.setDroneId(1);
        loadingRequestDto.setMedications(Collections.singletonList(
                new MedicationQuantityRequestDto(1, 30)
        ));

        Medication medication = new Medication();
        medication.setId(1);
        medication.setWeight((short) 50);
        List<Medication> medications = Collections.singletonList(medication);

        Mockito.when(droneRepository.findById(1)).thenReturn(java.util.Optional.of(drone));
        when(medicationService.getMedicationsByIdsList(any())).thenReturn(medications);
        // act & assert
        assertThrows(RuntimeException.class, () -> droneService.loadDrone(loadingRequestDto), "Can't load drone. Overweight.");
    }

    @Test
    void testGetDroneLoading() {
        int droneId = 1;
        Drone drone = new Drone();
        drone.setId(droneId);

        when(droneRepository.findById(anyInt())).thenReturn(Optional.of(drone));
        when(loadingRepository.getLoadingsByLoadingPK_DroneId(anyInt())).thenReturn(Collections.emptyList());

        DroneLoadingResponseDto result = droneService.getDroneLoading(droneId);

        assertEquals(droneId, result.getDrone().getId());
        assertEquals(0, result.getMedications().size());
        assertEquals(0, result.getSummaryWeight().intValue());
    }

    @Test
    public void testGetBatteryLevel() {
        Integer droneId = 1;
        Drone drone = new Drone();
        drone.setBatteryCapacity((byte) 50);

        when(droneRepository.findById(droneId)).thenReturn(java.util.Optional.of(drone));

        Integer batteryLevel = droneService.getBatteryLevel(droneId);

        assertEquals(50, batteryLevel.intValue());
    }

}