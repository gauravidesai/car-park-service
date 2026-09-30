package com.gauravidesai.car_park_service.exception;

/**
 * Exception thrown when parking is full.
 */
public class ParkingFullException extends RuntimeException {
    public ParkingFullException() {
        super();
    }
}
