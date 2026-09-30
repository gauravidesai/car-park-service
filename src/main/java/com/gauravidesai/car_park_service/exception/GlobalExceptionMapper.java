package com.gauravidesai.car_park_service.exception;


import com.gauravidesai.car_park_service.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Global exception handler for parking-related errors, maps custom exceptions to meaningful HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionMapper {
    /**
     * Handles cases where the parking lot has no available spaces.
     */
    @ExceptionHandler(ParkingFullException.class)
    public ResponseEntity<ErrorResponse> handleParkingFull(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        "Parking is full",
                        request.getRequestURI(),
                        LocalDateTime.now()
                ));
    }

    /**
     * Handles cases where a requested vehicle is not found in the system.
     */
    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleVehicleNotFound(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        "Vehicle not found",
                        request.getRequestURI(),
                        LocalDateTime.now()
                ));
    }

    /**
     * Handles any unexpected server-side errors.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        "An unexpected error occurred",
                        request.getRequestURI(),
                        LocalDateTime.now()
                ));
    }

    /**
     * Handles invalid operations such as duplicate vehicle parking.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex,
                                                               HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        ex.getMessage(),
                        request.getRequestURI(),
                        LocalDateTime.now()
                ));
    }

    /**
     * Handles validation errors triggered by invalid request payloads.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                          HttpServletRequest request) {
        String error = ex.getBindingResult().getFieldError().getDefaultMessage();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(error, request.getRequestURI(), LocalDateTime.now()));
    }

}