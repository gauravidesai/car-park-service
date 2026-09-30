package com.gauravidesai.car_park_service.exception;

/**
 * Exception thrown when an operation is attempted on a vehicle
 * that is not currently registered in the parking lot.
 */
public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException() {
        super();
    }
}
