package com.gauravidesai.car_park_service.Service;

import com.gauravidesai.car_park_service.dto.BillResponse;
import com.gauravidesai.car_park_service.dto.OccupiedParkingResponse;
import com.gauravidesai.car_park_service.exception.ParkingFullException;
import com.gauravidesai.car_park_service.exception.VehicleNotFoundException;
import com.gauravidesai.car_park_service.model.ParkingRecord;
import com.gauravidesai.car_park_service.model.VehicleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service handling all car park operations such as parking, billing and status.
 */
@Slf4j
@Service
public class CarParkingService {
    private int MAX_SPACES;

    /**
     * Returns the configured parking lot capacity.
     */
    public int getCapacity() {
        return MAX_SPACES;
    }

    private final Map<String, ParkingRecord> parkedVehicles = new ConcurrentHashMap<>();
    private final PriorityQueue<Integer> availableSpaces = new PriorityQueue<>();

    /**
     * Initializes the parking service with the given capacity.
     */
    public CarParkingService(@Value("${carpark.capacity}") int maxSpaces) {
        this.MAX_SPACES = maxSpaces;

        log.info("Initializing parking lot with capacity={}", maxSpaces);

        for (int i = 1; i <= MAX_SPACES; i++) {
            availableSpaces.offer(i);
        }

        log.debug("Available spaces initialized: {}", availableSpaces);
    }

    /**
     * Returns the current parking lot status including available and occupied spaces.
     */
    public OccupiedParkingResponse getStatus() {
        log.info("Fetching parking lot status");

        OccupiedParkingResponse response =
                new OccupiedParkingResponse(availableSpaces.size(), parkedVehicles.size());

        log.debug("Status: availableSpaces={}, occupiedSpaces={}",
                response.availableSpaces(), response.occupiedSpaces());

        return response;
    }

    /**
     * Parks a vehicle and assigns the next available space.
     * Throws an exception if the vehicle is already parked or the lot is full.
     */
    public synchronized ParkingRecord parkVehicle(String vehicleReg, int vehicleType) {
        log.info("Attempting to park vehicle: reg={}, type={}", vehicleReg, vehicleType);

        if (parkedVehicles.containsKey(vehicleReg)) {
            log.warn("Duplicate parking attempt for vehicleReg={}", vehicleReg);
            throw new IllegalArgumentException("Vehicle already parked");
        }

        if (availableSpaces.isEmpty()) {
            log.error("Parking full — cannot park vehicleReg={}", vehicleReg);
            throw new ParkingFullException();
        }

        int space = availableSpaces.poll();
        ParkingRecord record = new ParkingRecord(
                vehicleReg,
                space,
                LocalDateTime.now(),
                VehicleType.fromCode(vehicleType)
        );

        parkedVehicles.put(vehicleReg, record);
        log.info("Vehicle parked: reg={}, space={}, timeIn={}",
                record.vehicleReg(), record.spaceNumber(), record.timeIn());

        return record;
    }

    /**
     * Processes vehicle exit and calculates the parking bill.
     * Throws an exception if the vehicle is not found.
     */
    public synchronized BillResponse exitVehicle(String vehicleReg) {
        log.info("Processing exit for vehicleReg={}", vehicleReg);

        ParkingRecord record = parkedVehicles.remove(vehicleReg);

        if (record == null) {
            log.warn("Exit attempted for non-existent vehicleReg={}", vehicleReg);
            throw new VehicleNotFoundException();
        }

        availableSpaces.offer(record.spaceNumber());
        log.debug("Space {} returned to available pool", record.spaceNumber());

        LocalDateTime timeout = LocalDateTime.now();
        long minutesParked = Duration.between(record.timeIn(), timeout).toMinutes();

        double baseCharge = minutesParked * record.vehicleType().getChargePerMinute();
        double extraCharge = (minutesParked / 5) * 1.0;
        double totalCharge = baseCharge + extraCharge;

        log.info("Bill calculated: reg={}, minutesParked={}, baseCharge={}, extraCharge={}, totalCharge={}",
                vehicleReg, minutesParked, baseCharge, extraCharge, totalCharge);

        return new BillResponse(
                UUID.randomUUID().toString(),
                vehicleReg,
                totalCharge,
                record.timeIn(),
                timeout
        );
    }

    /**
     * Resets the parking lot to its initial empty state.
     * Useful for testing and maintenance operations.
     */
    public void clearAll() {
        log.info("Resetting parking lot state");

        parkedVehicles.clear();
        availableSpaces.clear();

        for (int i = 1; i <= MAX_SPACES; i++) {
            availableSpaces.offer(i);
        }
        log.debug("Parking lot reset complete. Capacity restored to {}", MAX_SPACES);
    }
}
