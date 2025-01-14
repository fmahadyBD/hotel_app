package com.f.backend.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.f.backend.entity.Location;
import com.f.backend.service.LocationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Location>> getAllLocation() {
        List<Location> allLocation = locationService.getAllLocation();
        return ResponseEntity.ok(allLocation);
    }

    // more explore about the OjectMapper
    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> savedLocation(
            @RequestPart(value = "location") String locationJson,
            @RequestParam(value = "image") MultipartFile file) throws JsonMappingException, JsonProcessingException {

        Map<String, String> response = new HashMap<>();
        if (locationJson == null || locationJson.isEmpty() || file == null || file.isEmpty()) {

            response.put("message", "Invalid input data");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Location location = objectMapper.readValue(locationJson, Location.class);
        try {

            locationService.savedLocation(location, file);
            response.put("message ", "Location saved successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IOException exception) {

            response.put("message", "Error saving in the image");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        } catch (Exception e) {

            response.put("message", "Error saving location: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteLocation(@PathVariable int id) {
        Map<String, String> response = new HashMap<>();
        try {
            locationService.deleteLocation(id);
            response.put("message", "Location deleted, id:" + id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Error dekete location: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, String>> updateLocation(
            @PathVariable int id,
            @RequestPart(value = "location") String locationJson,
            @RequestParam(value = "image", required = false) MultipartFile file)
            throws JsonMappingException, JsonProcessingException {

        Map<String, String> response = new HashMap<>();

        // Validate inputs
        if (locationJson == null || locationJson.isEmpty() || id <= 0) {
            response.put("message", "Invalid data: location JSON or ID is missing.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (file == null || file.isEmpty()) {
            response.put("message", "Invalid data: Image file is missing.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Location newLocation = objectMapper.readValue(locationJson, Location.class);

        try {
            Location location = locationService.upLocation(id, newLocation, file);
            response.put("message", "Location updated successfully.");
            response.put("locationId", String.valueOf(location.getId()));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFoundException e) {
            response.put("message", "Location not found: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IOException e) {
            response.put("message", "Error processing the image: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            response.put("message", "Unexpected error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getLocationById(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Location location = locationService.getLocationById(id);
            response.put("message", "Found the entity");
            response.put("message", location);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFoundException e) {
            response.put("message", "Error finding location: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("message", "An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}