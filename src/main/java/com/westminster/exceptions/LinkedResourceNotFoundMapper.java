package com.westminster.exceptions;

import jakarta.ws.rs.core.*;
import jakarta.ws.rs.ext.*;
import java.util.Map;

@Provider
public class LinkedResourceNotFoundMapper implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException exception) {

        String errorMessage = exception.getMessage();

        return buildUnprocessableResponse(errorMessage);
    }

    // 🔹 Helper method (consistent with your other mappers)
    private Response buildUnprocessableResponse(String messageText) {
        return Response.status(Response.Status.fromStatusCode(422))
                .entity(Map.of(
                        "error", "Unprocessable Entity",
                        "message", messageText
                ))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}