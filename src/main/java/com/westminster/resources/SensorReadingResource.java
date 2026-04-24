package com.westminster.resources;

import com.westminster.exceptions.SensorUnavailableException;
import com.westminster.models.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.*;

public class SensorReadingResource {

    // Renamed field
    private final String targetSensorId;

    // Renamed storage structure
    private static final Map<String, List<SensorReading>> readingStore = new HashMap<>();

    public SensorReadingResource(String sensorIdentifier) {
        this.targetSensorId = sensorIdentifier;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchSensorReadings() {

        boolean sensorExists = SensorResource.sensorRegistry.containsKey(targetSensorId);

        if (!sensorExists) {
            return Response.status(404)
                    .entity(Map.of("error", "Sensor not found: " + targetSensorId))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        List<SensorReading> storedReadings =
                readingStore.getOrDefault(targetSensorId, new ArrayList<>());

        return Response.ok(storedReadings).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createReading(SensorReading newReading) {

        Sensor relatedSensor = SensorResource.sensorRegistry.get(targetSensorId);

        if (relatedSensor == null) {
            return Response.status(404)
                    .entity(Map.of("error", "Sensor not found: " + targetSensorId))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        String currentStatus = relatedSensor.getStatus();

        // Block if under maintenance
        if ("MAINTENANCE".equalsIgnoreCase(currentStatus)) {
            throw new SensorUnavailableException(
                    "Sensor '" + targetSensorId + "' is currently under MAINTENANCE " +
                    "and cannot accept new readings."
            );
        }

        // Initialize metadata (UUID + timestamp)
        newReading.initialise();

        // Store reading using computeIfAbsent (same logic, different naming)
        List<SensorReading> sensorHistory =
                readingStore.computeIfAbsent(targetSensorId, key -> new ArrayList<>());

        sensorHistory.add(newReading);

        // Update latest sensor value
        double latestValue = newReading.getValue();
        relatedSensor.setCurrentValue(latestValue);

        return Response.status(201)
                .entity(newReading)
                .build();
    }
}