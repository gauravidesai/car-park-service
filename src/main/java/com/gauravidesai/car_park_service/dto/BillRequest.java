package com.gauravidesai.car_park_service.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request for releasing a vehicle's space and issuing its bill.
 * @param vehicleReg the registration of the vehicle leaving (must not be blank)
 */
public record BillRequest (
        @NotBlank(message = "Vehicle registration is required")
        String vehicleReg
) {
}
