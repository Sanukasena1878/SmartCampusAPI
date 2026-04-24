package com.westminster.exceptions;

import jakarta.ws.rs.core.*;
import jakarta.ws.rs.ext.*;
import java.util.Map;

@Provider
public class SensorUnavailableMapper implements ExceptionMapper<SensorUnavailableException> {

    @Override
    public Response toResponse(SensorUnavailableException exception) {

        String errorDetails = exception.getMessage();

        return buildForbiddenResponse(errorDetails);
    }

    // 🔹 Helper method for cleaner structure
    private Response buildForbiddenResponse(String messageText) {
        return Response.status(Response.Status.FORBIDDEN)
                .entity(Map.of(
                        "error", "Forbidden",
                        "message", messageText
                ))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}