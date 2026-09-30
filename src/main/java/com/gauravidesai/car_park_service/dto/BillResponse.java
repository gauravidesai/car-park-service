package com.gauravidesai.car_park_service.dto;

import java.time.LocalDateTime;

/**
 * Bill issued when a vehicle leaves and its space is released.
 * @param billId         unique identifier for this bill
 * @param vehicleReg     the vehicle's registration
 * @param vehicleCharge  total charge, covering the per-minute rate plus £1 for every complete 5 minutes parked
 * @param timeIn         when the vehicle entered
 * @param timeOut        when the vehicle left
 */
public record BillResponse (
        String billId,
        String vehicleReg,
        double vehicleCharge,
        LocalDateTime timeIn,
        LocalDateTime timeOut
) {
}
