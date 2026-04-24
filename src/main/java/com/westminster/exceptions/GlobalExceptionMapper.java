package com.westminster.exceptions;

import jakarta.ws.rs.core.*;
import jakarta.ws.rs.ext.*;
import java.util.Map;
import java.util.logging.Logger;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    // Renamed logger
    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable exception) {

        String errorType = exception.getClass().getSimpleName();
        String errorMessage = exception.getMessage();

        // Log internally (not exposed to client)
        LOGGER.severe("Server failure detected -> Type: " + errorType + " | Message: " + errorMessage);

        return buildInternalErrorResponse();
    }

    // 🔹 Helper method (adds structure difference)
    private Response buildInternalErrorResponse() {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of(
                        "error", "Internal Server Error",
                        "message", "An unexpected error occurred. Please try again later."
                ))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}