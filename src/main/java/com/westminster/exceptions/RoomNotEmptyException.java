package com.westminster.exceptions;

public class RoomNotEmptyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RoomNotEmptyException(String errorDetails) {
        super(errorDetails);
    }

    // Additional constructor for flexibility
    public RoomNotEmptyException(String errorDetails, Throwable cause) {
        super(errorDetails, cause);
    }
}