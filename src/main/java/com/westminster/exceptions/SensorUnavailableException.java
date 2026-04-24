package com.westminster.exceptions;

public class SensorUnavailableException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SensorUnavailableException(String errorMessage) {
        super(errorMessage);
    }

    // Additional constructor for flexibility
    public SensorUnavailableException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}