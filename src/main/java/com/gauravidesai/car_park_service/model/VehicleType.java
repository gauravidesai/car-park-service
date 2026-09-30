package com.gauravidesai.car_park_service.model;

/**
 * Vehicle sizes accepted by the car park with its per-minute parking rate.
 */
public enum VehicleType {
    SMALL (1, 0.10),
    MEDIUM (2, 0.20),
    LARGE (3, 0.40);

    private final int code;
    private final double chargePerMinute;

    VehicleType(int code, double chargePerMinute) {
        this.code = code;
        this.chargePerMinute = chargePerMinute;
    }

    /**
     * @return the charge per minute parked, in pounds
     */
    public double getChargePerMinute() {
        return chargePerMinute;
    }

    /**
     * Resolves the vehicle type for code.
     * @param code 1 for SMALL, 2 for MEDIUM, 3 for LARGE
     * @return the matching vehicle type
     * @throws IllegalArgumentException if the code is not 1, 2 or 3
     */
    public static VehicleType fromCode(int code) {
        return switch(code) {
            case 1 -> SMALL;
            case 2 -> MEDIUM;
            case 3 -> LARGE;
            default -> throw new IllegalArgumentException("Invalid Vehicle Type");
        };
    }
}
