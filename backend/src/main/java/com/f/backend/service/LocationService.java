package com.f.backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.f.backend.entity.Location;
import com.f.backend.reposiotry.LocationRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Value("${image.upload.dir}")
    private String uploadDir;

    // Get the all location form database
    // Service Layer: Focus on business logic
    public List<Location> getAllLocation() {
        return locationRepository.findAll();
    }

    // Save new Location
    public void savedLocation(Location location, MultipartFile file) throws IOException {

        if (file != null && !file.isEmpty()) {
            String fileName = savedImage(file, location);
            location.setImage(fileName);
        }
        locationRepository.save(location);
    }

    // get Location by id
    public Location getLocationById(int id) {

        return locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location not found by this id: " + id));
    }

    // Update the Location
    public Location upLocation(int id, Location newLocation, MultipartFile file) throws IOException {
        Location oldLocation = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found any location with this id: " + id));

        if (newLocation.getName() != null) {
            oldLocation.setName(newLocation.getName());
        }
        if (file != null && !file.isEmpty()) {
            String filename = savedImage(file, newLocation);
            oldLocation.setImage(filename);
        }
        return locationRepository.save(oldLocation);
    }

    // Delete location
    public void deleteLocation(int id) {
        locationRepository.deleteById(id);
    }

    public String savedImage(MultipartFile image, Location location) throws IOException {

        Path uploadPath = Paths.get(uploadDir + "/locations");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileName = location.getName() + "_" + UUID.randomUUID().toString();

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(image.getInputStream(), filePath);
        return fileName;

    }

}
