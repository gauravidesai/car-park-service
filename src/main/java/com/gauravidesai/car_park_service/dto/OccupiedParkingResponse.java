package com.gauravidesai.car_park_service.dto;

/**
 * Current occupancy of the car park.
 * @param availableSpaces number of available spaces
 * @param occupiedSpaces  number of occupied spaces
 */
public record OccupiedParkingResponse (
        int availableSpaces,
        int occupiedSpaces
) {
}
