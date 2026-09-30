package com.gauravidesai.car_park_service.dto;

import java.time.LocalDateTime;

/**
 * Response returned after a vehicle is parked.
 * @param vehicleReg  the vehicle's registration
 * @param spaceNumber the allocated space number
 * @param timeIn      when the vehicle entered (from which charging starts)
 */
public record ParkingResponse (
        String vehicleReg,
        int spaceNumber,
        LocalDateTime timeIn
) {
}
