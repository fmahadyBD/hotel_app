package com.f.backend.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.f.backend.entity.Hotel;
import com.f.backend.entity.Location;
import com.f.backend.reposiotry.HotelRepository;
import com.f.backend.reposiotry.LocationRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Value("${image.upload.dir}")
    private String uploadDir;

    /**
     * Fetches all hotels from the repository.
     *
     * @return List of all hotels.
     */
    public List<Hotel> getAllHotel() {
        return hotelRepository.findAll();
    }

    /**
     * Saves a hotel entity with optional image file upload.
     *
     * @param hotel      Hotel entity to save.
     * @param imageFile  MultipartFile representing the image file.
     * @throws IOException if there's an error during file upload.
     */
    public Hotel saveHotel(Hotel hotel, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageFileName = saveImage(imageFile, hotel);
            hotel.setImage(imageFileName);
        }

        // Validate and set location
        Location location = locationRepository.findById(hotel.getLocation().getId())
                .orElseThrow(() -> new EntityNotFoundException("Location not found with ID: " + hotel.getLocation().getId()));
        hotel.setLocation(location);

        return hotelRepository.save(hotel);
    }

    /**
     * Finds a hotel by its ID.
     *
     * @param id Hotel ID.
     * @return Hotel entity.
     * @throws EntityNotFoundException if the hotel is not found.
     */
    public Hotel findHotelById(int id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with ID: " + id));
    }

    /**
     * Finds a hotel by its name.
     *
     * @param name Hotel name.
     * @return Hotel entity.
     * @throws EntityNotFoundException if the hotel is not found.
     */
    public Hotel findHotelByName(String name) {
        return hotelRepository.findHotelByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with name: " + name));
    }

    /**
     * Deletes a hotel by its ID.
     *
     * @param id Hotel ID.
     * @throws EntityNotFoundException if the hotel does not exist.
     */
    public void deleteHotelById(int id) {
        if (!hotelRepository.existsById(id)) {
            throw new EntityNotFoundException("Hotel not found with ID: " + id);
        }
        hotelRepository.deleteById(id);
    }

    /**
     * Saves an image to the filesystem and returns its filename.
     *
     * @param file  MultipartFile representing the image file.
     * @param hotel Hotel entity associated with the image.
     * @return Filename of the saved image.
     * @throws IOException if there's an error during file saving.
     */
    private String saveImage(MultipartFile file, Hotel hotel) throws IOException {
        Path uploadPath = Paths.get(uploadDir, "hotels");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = hotel.getName() + "_" + UUID.randomUUID();
        Path filePath = uploadPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath);
        return fileName;
    }

    /**
     * Updates an existing hotel with new details and optional image upload.
     *
     * @param id          Hotel ID to update.
     * @param updateHotel Updated hotel details.
     * @param image       MultipartFile representing the new image file.
     * @return Updated hotel entity.
     * @throws IOException if there's an error during file upload.
     */
    public Hotel updateHotel(int id, Hotel updateHotel, MultipartFile image) throws IOException {
        Hotel existingHotel = hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with ID: " + id));

        // Update fields if provided
        if (updateHotel.getName() != null) {
            existingHotel.setName(updateHotel.getName());
        }
        if (updateHotel.getAddress() != null) {
            existingHotel.setAddress(updateHotel.getAddress());
        }
        if (updateHotel.getRating() != null) {
            existingHotel.setRating(updateHotel.getRating());
        }
        if (updateHotel.getMaximum_price() > 0) {
            existingHotel.setMaximum_price(updateHotel.getMaximum_price());
        }
        if (updateHotel.getMinimum_price() > 0) {
            existingHotel.setMinimum_price(updateHotel.getMinimum_price());
        }

        // Update location
        Location location = locationRepository.findById(updateHotel.getLocation().getId())
                .orElseThrow(() -> new EntityNotFoundException("Location not found with ID: " + updateHotel.getLocation().getId()));
        existingHotel.setLocation(location);

        // Update image if provided
        if (image != null && !image.isEmpty()) {
            String fileName = saveImage(image, updateHotel);
            existingHotel.setImage(fileName);
        }

        return hotelRepository.save(existingHotel);
    }

    /**
     * Finds hotels by location name.
     *
     * @param locationName Name of the location.
     * @return List of hotels in the specified location.
     */
    public List<Hotel> findHotelByLocationName(String locationName) {
        return hotelRepository.findHotelByLocationName(locationName);
    }
}
