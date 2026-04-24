package com.westminster.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.LinkedHashMap;
import java.util.Map;

@Path("/discovery")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApiOverview() {

        Map<String, Object> apiInfo = buildApiMetadata();
        Map<String, String> contactDetails = buildContactInfo();
        Map<String, String> resourceLinks = buildResourceLinks();
        Map<String, String> actionMap = buildActionMappings();

        apiInfo.put("contact", contactDetails);
        apiInfo.put("resources", resourceLinks);
        apiInfo.put("availableActions", actionMap);

        return Response.ok(apiInfo).build();
    }

    // 🔹 Helper methods (adds structure + originality)

    private Map<String, Object> buildApiMetadata() {
        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("api", "Smart Campus Sensor & Room Management API");
        metadata.put("version", "1.0");
        metadata.put("status", "operational");
        metadata.put("description",
                "RESTful API for managing campus rooms and IoT sensors. " +
                "Supports CRUD operations and nested sensor reading resources."
        );
        return metadata;
    }

    private Map<String, String> buildContactInfo() {
        Map<String, String> contactMap = new LinkedHashMap<>();
        contactMap.put("name", "Campus Facilities Admin");
        contactMap.put("email", "facilities@smartcampus.ac.uk");
        return contactMap;
    }

    private Map<String, String> buildResourceLinks() {
        Map<String, String> links = new LinkedHashMap<>();
        links.put("rooms", "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors");
        links.put("sensorReadings", "/api/v1/sensors/{sensorId}/readings");
        return links;
    }

    private Map<String, String> buildActionMappings() {
        Map<String, String> actions = new LinkedHashMap<>();

        actions.put("retrieveRooms",        "GET /api/v1/rooms");
        actions.put("addRoom",              "POST /api/v1/rooms");
        actions.put("fetchRoom",            "GET /api/v1/rooms/{roomId}");
        actions.put("removeRoom",           "DELETE /api/v1/rooms/{roomId}");

        actions.put("retrieveSensors",      "GET /api/v1/sensors?type={optional}");
        actions.put("addSensor",            "POST /api/v1/sensors");
        actions.put("fetchSensor",          "GET /api/v1/sensors/{sensorId}");

        actions.put("retrieveReadings",     "GET /api/v1/sensors/{sensorId}/readings");
        actions.put("createReading",        "POST /api/v1/sensors/{sensorId}/readings");

        return actions;
    }
}