package com.musala.drones.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "battery_level_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BatteryLevelLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "drone_id")
    private Integer droneId;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "battery_level")
    private Byte batteryLevel;

}
