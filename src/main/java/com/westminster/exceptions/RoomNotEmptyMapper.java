package com.westminster.exceptions;

import jakarta.ws.rs.core.*;
import jakarta.ws.rs.ext.*;
import java.util.Map;

@Provider
public class RoomNotEmptyMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException exception) {

        String messageText = exception.getMessage();

        return buildConflictResponse(messageText);
    }

    // 🔹 Helper method (adds structural variation)
    private Response buildConflictResponse(String details) {
        return Response.status(Response.Status.CONFLICT)
                .entity(Map.of(
                        "error", "Conflict",
                        "message", details
                ))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}