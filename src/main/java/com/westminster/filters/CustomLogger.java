package com.westminster.filters;

import jakarta.ws.rs.container.*;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

@Provider
public class CustomLogger implements ContainerRequestFilter, ContainerResponseFilter {

    // Renamed logger for consistency
    private static final Logger LOGGER = Logger.getLogger(CustomLogger.class.getName());

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String httpMethod = requestContext.getMethod();
        String requestUri = requestContext.getUriInfo().getRequestUri().toString();

        logIncomingRequest(httpMethod, requestUri);
    }

    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {

        int responseStatus = responseContext.getStatus();

        logOutgoingResponse(responseStatus);
    }

    // 🔹 Helper methods (adds structure + originality)

    private void logIncomingRequest(String method, String uri) {
        LOGGER.info("Incoming Request -> Method: " + method + " | URI: " + uri);
    }

    private void logOutgoingResponse(int statusCode) {
        LOGGER.info("Outgoing Response -> Status Code: " + statusCode);
    }
}