package com.gauravidesai.car_park_service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * Request body for parking a vehicle.
 * @param vehicleReg  the vehicle's registration; must not be blank
 * @param vehicleType 1 for small, 2 for medium, 3 for large
 */
public record ParkingRequest (
        @NotBlank(message = "Vehicle registration is required")
        String vehicleReg,

        @Min(value = 1, message = "Vehicle type must be 1 (small), 2 (medium) or 3 (large)")
        @Max(value = 3, message = "Vehicle type must be 1 (small), 2 (medium) or 3 (large)")
        int vehicleType
) {
}
