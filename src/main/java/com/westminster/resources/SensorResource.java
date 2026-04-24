package com.westminster.resources;

import com.westminster.exceptions.LinkedResourceNotFoundException;
import com.westminster.models.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Path("/sensors")
public class SensorResource {

    // Renamed storage
    public static final Map<String, Sensor> sensorRegistry = new ConcurrentHashMap<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveSensors(@QueryParam("type") String sensorType) {

        List<Sensor> sensorCollection = new ArrayList<>(sensorRegistry.values());

        // Apply filtering if query param exists
        if (sensorType != null && !sensorType.isBlank()) {
            sensorCollection.removeIf(currentSensor ->
                    !currentSensor.getType().equalsIgnoreCase(sensorType));
        }

        return Response.ok(sensorCollection).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerSensor(Sensor newSensor) {

        String sensorKey = newSensor.getId();

        if (sensorKey == null || sensorKey.isBlank()) {
            return Response.status(400)
                    .entity(Map.of("error", "Sensor 'id' field is required"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        // Validate linked room existence
        String linkedRoomId = newSensor.getRoomId();
        Room associatedRoom = RoomResource.roomStore.get(linkedRoomId);

        if (associatedRoom == null) {
            throw new LinkedResourceNotFoundException(
                    "Room with id '" + linkedRoomId + "' does not exist. " +
                    "Create the room before assigning sensors to it."
            );
        }

        sensorRegistry.put(sensorKey, newSensor);

        // Maintain relationship
        List<String> roomSensors = associatedRoom.getSensorIds();
        roomSensors.add(sensorKey);

        return Response.status(201)
                .entity(newSensor)
                .build();
    }

    // Sub-resource locator
    @Path("/{id}/readings")
    public SensorReadingResource routeToReadings(@PathParam("id") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}