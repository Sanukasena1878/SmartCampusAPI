package com.westminster.exceptions;

public class LinkedResourceNotFoundException extends RuntimeException {

    // Default serial version UID (adds professionalism)
    private static final long serialVersionUID = 1L;

    public LinkedResourceNotFoundException(String errorMessage) {
        super(errorMessage);
    }

    // Optional second constructor (same behavior, more flexible design)
    public LinkedResourceNotFoundException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}