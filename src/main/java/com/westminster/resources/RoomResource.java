package com.westminster.resources;

import com.westminster.exceptions.RoomNotEmptyException;
import com.westminster.models.Room;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Path("/rooms")
public class RoomResource {

    // Renamed storage map
    public static final Map<String, Room> roomStore = new ConcurrentHashMap<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchAllRooms() {
        List<Room> roomList = new ArrayList<>(roomStore.values());
        return Response.ok(roomList).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRoom(Room incomingRoom) {

        String roomIdentifier = incomingRoom.getId();

        if (roomIdentifier == null || roomIdentifier.isBlank()) {
            return Response.status(400)
                    .entity(Map.of("error", "Room 'id' field is required"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        roomStore.put(roomIdentifier, incomingRoom);

        return Response.status(201)
                .entity(incomingRoom)
                .build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchRoomById(@PathParam("id") String roomId) {

        Room foundRoom = roomStore.get(roomId);

        if (foundRoom == null) {
            return Response.status(404)
                    .entity(Map.of("error", "Room not found: " + roomId))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        return Response.ok(foundRoom).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeRoom(@PathParam("id") String targetId) {

        Room selectedRoom = roomStore.get(targetId);

        // Still idempotent
        if (selectedRoom == null) {
            return Response.noContent().build();
        }

        List<String> assignedSensors = selectedRoom.getSensorIds();

        if (!assignedSensors.isEmpty()) {
            throw new RoomNotEmptyException(
                    "Cannot delete room '" + targetId + "'. It still has " +
                    assignedSensors.size() + " sensor(s) assigned to it. " +
                    "Remove all sensors first."
            );
        }

        roomStore.remove(targetId);

        return Response.noContent().build();
    }
}