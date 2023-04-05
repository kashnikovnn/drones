package com.musala.drones.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musala.drones.dto.DroneDto;
import com.musala.drones.dto.MedicationDto;
import com.musala.drones.dto.loading.DroneLoadingRequestDto;
import com.musala.drones.dto.loading.DroneLoadingResponseDto;
import com.musala.drones.dto.loading.MedicationQuantityResponseDto;
import com.musala.drones.model.Drone;
import com.musala.drones.model.Loading;
import com.musala.drones.model.LoadingPK;
import com.musala.drones.model.Medication;
import com.musala.drones.model.enums.DroneModel;
import com.musala.drones.model.enums.DroneState;
import com.musala.drones.service.DroneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class DroneControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DroneService droneService;

    @InjectMocks
    private DroneController droneController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(droneController).build();
    }

    @Test
    void registerDrone_returnsOk() throws Exception {
        DroneDto droneDto = new DroneDto();
        droneDto.setSerialNumber("serialNumber");
        droneDto.setModel("model");
        droneDto.setWeightLimit(100);
        droneDto.setBatteryCapacity(50);
        droneDto.setState(DroneState.IDLE.name());

        doNothing().when(droneService).registerDrone(any(DroneDto.class));

        mockMvc.perform(post("/api/drones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(droneDto)))
                .andExpect(status().isOk());
    }

    @Test
    void getDrone_returnsDroneDto() throws Exception {
        Drone drone = new Drone();
        drone.setId(1);
        drone.setSerialNumber("serialNumber");
        drone.setModel(DroneModel.LIGHTWEIGHT);
        drone.setWeightLimit((short) 100);
        drone.setBatteryCapacity((byte) 50);
        drone.setState(DroneState.IDLE);

        DroneDto droneDto = new DroneDto();
        droneDto.setId(1);
        droneDto.setSerialNumber("serialNumber");
        droneDto.setModel(DroneModel.LIGHTWEIGHT.name());
        droneDto.setWeightLimit(100);
        droneDto.setBatteryCapacity(50);
        droneDto.setState(DroneState.IDLE.name());

        when(droneService.getDroneDto(1)).thenReturn(droneDto);

        mockMvc.perform(get("/api/drones/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.serialNumber").value("serialNumber"))
                .andExpect(jsonPath("$.model").value(DroneModel.LIGHTWEIGHT.name()))
                .andExpect(jsonPath("$.weightLimit").value(100))
                .andExpect(jsonPath("$.batteryCapacity").value(50))
                .andExpect(jsonPath("$.state").value(DroneState.IDLE.name()));
    }

    @Test
    void testGetLoadingAvailableDrones() throws Exception {
        // given
        List<DroneDto> drones = List.of(
                new DroneDto(1, "SN1", "M1", 100, 50, DroneState.IDLE.name()),
                new DroneDto(2, "SN2", "M2", 200, 75, DroneState.LOADING.name())
        );
        when(droneService.getLoadingAvailableDrones()).thenReturn(drones);

        // when
        mockMvc.perform(get("/api/drones/loading-available")
                        .contentType(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(drones.get(0).getId())))
                .andExpect(jsonPath("$[0].serialNumber", is(drones.get(0).getSerialNumber())))
                .andExpect(jsonPath("$[0].model", is(drones.get(0).getModel())))
                .andExpect(jsonPath("$[0].weightLimit", is(drones.get(0).getWeightLimit())))
                .andExpect(jsonPath("$[0].batteryCapacity", is(drones.get(0).getBatteryCapacity())))
                .andExpect(jsonPath("$[0].state", is(drones.get(0).getState())))
                .andExpect(jsonPath("$[1].id", is(drones.get(1).getId())))
                .andExpect(jsonPath("$[1].serialNumber", is(drones.get(1).getSerialNumber())))
                .andExpect(jsonPath("$[1].model", is(drones.get(1).getModel())))
                .andExpect(jsonPath("$[1].weightLimit", is(drones.get(1).getWeightLimit())))
                .andExpect(jsonPath("$[1].batteryCapacity", is(drones.get(1).getBatteryCapacity())))
                .andExpect(jsonPath("$[1].state", is(drones.get(1).getState())));

        verify(droneService, times(1)).getLoadingAvailableDrones();
        verifyNoMoreInteractions(droneService);
    }


    @Test
    public void testGetBatteryLevel() throws Exception {
        when(droneService.getBatteryLevel(anyInt())).thenReturn(90);

        mockMvc.perform(get("/api/drones/1/battery-level"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(90));
    }

    @Test
    public void testLoadDrone() throws Exception {
        DroneLoadingRequestDto requestDto = new DroneLoadingRequestDto();
        requestDto.setDroneId(1);
        requestDto.setMedications(new ArrayList<>());

        mockMvc.perform(post("/api/drones/loading")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        // assert that the service method was called with the correct parameters
        verify(droneService).loadDrone(requestDto);
    }

    @Test
    public void testGetDroneLoading() throws Exception {
        // mock drone, medication and loading data
        Drone drone = new Drone();
        drone.setId(1);
        drone.setSerialNumber("SN123");
        drone.setWeightLimit((short) 500);
        drone.setBatteryCapacity((byte) 100);
        drone.setState(DroneState.LOADING);

        Medication medication = new Medication();
        medication.setId(1);
        medication.setName("Medication 1");
        medication.setWeight((short) 10);
        medication.setCode("CODE1");

        Loading loading = new Loading();
        loading.setLoadingPK(new LoadingPK(drone, medication));
        loading.setQty(10);

        // set up expected response
        DroneDto droneDto = new DroneDto(1, "SN123", "Lightweight", 500, 100, "LOADING");
        MedicationQuantityResponseDto medicationDto = new MedicationQuantityResponseDto(new MedicationDto(1, "Medication 1", 10, "CODE1", null), 10);
        DroneLoadingResponseDto expectedResponse = new DroneLoadingResponseDto(droneDto, List.of(medicationDto), 100);

        // mock drone service method
        when(droneService.getDroneLoading(anyInt())).thenReturn(expectedResponse);

        // perform request and assert response
        mockMvc.perform(get("/api/drones/1/loading")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.drone.id").value(1))
                .andExpect(jsonPath("$.drone.serialNumber").value("SN123"))
                .andExpect(jsonPath("$.medications[0].medication.name").value("Medication 1"))
                .andExpect(jsonPath("$.medications[0].quantity").value(10))
                .andExpect(jsonPath("$.summaryWeight").value(100));
    }


}
