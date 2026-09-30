package com.gauravidesai.car_park_service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Request body for parking a vehicle.
 * @param vehicleReg  the vehicle's registration; must not be blank
 * @param vehicleType 1 for small, 2 for medium, 3 for large
 */
public record ParkingRequest (
        @NotBlank(message = "Vehicle registration is required")
        @Pattern(regexp = "^[A-Za-z0-9]{1,10}$",
                message = "Vehicle registration must be alphanumeric and up to 10 characters")
        String vehicleReg,

        @Min(value = 1, message = "Vehicle type must be 1 (small), 2 (medium) or 3 (large)")
        @Max(value = 3, message = "Vehicle type must be 1 (small), 2 (medium) or 3 (large)")
        int vehicleType
) {
}
