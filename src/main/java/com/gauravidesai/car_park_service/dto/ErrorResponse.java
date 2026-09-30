package com.gauravidesai.car_park_service.dto;

import java.time.LocalDateTime;

/**
 * Standard error response returned for API exceptions.
 * @param message   human-readable error message
 * @param path      the request path where the error occurred
 * @param timestamp time when the error was generated
 */
public record ErrorResponse(
        String message,
        String path,
        LocalDateTime timestamp
) {}
