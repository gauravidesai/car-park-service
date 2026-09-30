package com.gauravidesai.car_park_service.model;

import java.time.LocalDateTime;

/**
 * A vehicle currently occupying a space in the car park.
 * @param vehicleReg   the vehicle's registration
 * @param spaceNumber  the allocated space
 * @param timeIn       when the vehicle entered, used as the start point for charging
 * @param vehicleType  the vehicle's type, which determines the per-minute rate
 */
public record ParkingRecord(
        String vehicleReg,
        int spaceNumber,
        LocalDateTime timeIn,
        VehicleType vehicleType
) {}