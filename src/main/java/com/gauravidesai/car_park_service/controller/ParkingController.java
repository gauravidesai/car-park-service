package com.gauravidesai.car_park_service.controller;

import com.gauravidesai.car_park_service.Service.CarParkingService;
import com.gauravidesai.car_park_service.dto.*;
import com.gauravidesai.car_park_service.model.ParkingRecord;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing parking operations.
 * Provides endpoints to check parking status, park vehicles, and generate bills.
 */
@Slf4j
@RestController
@RequestMapping("/parking")
@Tag(
        name = "Parking Management",
        description = "Operations for parking vehicles, checking status, and generating bills"
)
public class ParkingController {

    private final CarParkingService carParkingService;

    /**
     * Creates a new ParkingController with the required parking service.
     */
    public ParkingController(CarParkingService carParkingService) {
        this.carParkingService = carParkingService;
    }

    /**
     * Returns the current parking lot status including available and occupied spaces.
     */
    @Operation(
            summary = "Get parking lot status",
            description = "Returns the number of available and occupied parking spaces."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status fetched successfully")
    })
    @GetMapping
    public OccupiedParkingResponse getParkingResponse() {
        log.info("Fetching parking lot status");
        OccupiedParkingResponse response = carParkingService.getStatus();
        log.debug("Status fetched: availableSpaces={}, occupiedSpaces={}",
                response.availableSpaces(), response.occupiedSpaces());
        return response;
    }

    /**
     * Parks a vehicle and returns the assigned space and entry time.
     */
    @Operation(
            summary = "Park a vehicle",
            description = "Assigns a parking space to the vehicle and returns parking details."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehicle parked successfully"),
            @ApiResponse(responseCode = "400", description = "Parking is full or invalid request")
    })
    @PostMapping
    public ParkingResponse parkVehicle(@Valid @RequestBody ParkingRequest request) {
        log.info("Received parking request for vehicleReg={}, vehicleType={}",
                request.vehicleReg(), request.vehicleType());

        ParkingRecord record = carParkingService.parkVehicle(
                request.vehicleReg(),
                request.vehicleType()
        );

        log.info("Vehicle parked: reg={}, space={}, timeIn={}",
                record.vehicleReg(), record.spaceNumber(), record.timeIn());

        return new ParkingResponse(record.vehicleReg(), record.spaceNumber(), record.timeIn());
    }

    /**
     * Processes vehicle exit and returns the calculated parking bill.
     */
    @Operation(
            summary = "Generate bill for exiting vehicle",
            description = "Calculates total parking charges based on duration and vehicle type."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bill generated successfully"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @PostMapping("/bill")
    public BillResponse chargeVehicle(@RequestBody BillRequest request) {
        log.info("Processing exit for vehicleReg={}", request.vehicleReg());

        BillResponse bill = carParkingService.exitVehicle(request.vehicleReg());

        log.info("Bill generated: reg={}, totalCharge={}, timeIn={}, timeOut={}",
                bill.vehicleReg(), bill.vehicleCharge(), bill.timeIn(), bill.timeOut());

        return bill;
    }
}