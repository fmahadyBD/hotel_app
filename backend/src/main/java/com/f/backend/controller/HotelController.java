package com.f.backend.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.f.backend.entity.Hotel;
import com.f.backend.service.HotelService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/hotel")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping("/")
    public ResponseEntity<List<Hotel>> getAllHotels() {
        List<Hotel> hotels = hotelService.getAllHotel();
        return ResponseEntity.ok(hotels);
    }

    /**
     * @param hotelJson
     * @param file
     * @return
     * @throws JsonProcessingException Post: URL
     *                                 form-data
     *                                 key value Content type
     *                                 hotel json data application/json
     *                                 image give the file multipart/form-data
     */

    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> saveHotel(
            @RequestPart(value = "hotel") String hotelJson,
            @RequestParam(value = "image") MultipartFile file) throws JsonProcessingException, IOException {

        Map<String, String> response = new HashMap<>();

        // Validate hotel JSON
        if (hotelJson == null || hotelJson.isEmpty()) {
            response.put("message", "Invalid data: Hotel JSON is missing or empty.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Validate image file
        if (file == null || file.isEmpty()) {
            response.put("message", "Invalid data: Hotel image file is missing or empty.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Hotel hotel = objectMapper.readValue(hotelJson, Hotel.class);

        try {
            // Save the hotel and file using your service method
            Hotel savedHotel = hotelService.saveHotel(hotel, file);

            response.put("message", "Hotel added successfully!");
            // response.put("hotelId", String.valueOf(savedHotel.getId())); // Uncomment
            // when ready
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            response.put("message", "Unexpected error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable int id) {
        try {
            Hotel hotel = hotelService.findHotelById(id);
            return ResponseEntity.ok(hotel);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteHotel(@PathVariable int id) {
        try {
            hotelService.deleteHotelById(id);
            return ResponseEntity.ok("Hotel with this id " + id + "has been deleted! ");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/h/searchhotel")
    public ResponseEntity<List<Hotel>> findHotelByLocationName(
            @RequestParam(value = "locationName") String locationName) {
        try {
            List<Hotel> hotels = hotelService.findHotelByLocationName(locationName);

            if (hotels.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            return ResponseEntity.ok(hotels);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/h/searchhotelname")
    public ResponseEntity<Hotel> findHotelByName(@RequestParam(value = "name") String name) {
        try {
            Hotel hotel = hotelService.findHotelByName(name);
            return ResponseEntity.ok(hotel);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // Hotel not found
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Unexpected error
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Hotel> updateHotel(
            @PathVariable int id,
            @RequestPart Hotel hotel,
            @RequestParam(value = "image", required = true) MultipartFile file) throws IOException {
        Hotel updateHotel = hotelService.updateHotel(id, hotel, file);
        return ResponseEntity.ok(updateHotel);

    }

    // @GetMapping("/")
    // public ResponseEntity<ApiResponse> getAllHotel() {
    // try {
    // List<Hotel> allHotels = hotelService.getAllHotel();
    // return ResponseEntity.status(OK).body(new ApiResponse("All Hotel Data",
    // allHotels));
    // } catch (ResourceNotFound e) {
    // return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),
    // null));
    // }

    // }

    @GetMapping("/hotel-by-location-id/{id}")
    public ResponseEntity<List<Hotel>> getHotelByLocationId(@PathVariable int id){
            List<Hotel> hotels= hotelService.getHotelByLocationId(id);
            return ResponseEntity.ok(hotels);

    }
}
