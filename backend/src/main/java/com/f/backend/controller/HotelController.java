package com.f.backend.controller;

import com.f.backend.custom_exception.ResourceNotFound;
import com.f.backend.entity.Hotel;
import com.f.backend.response.ApiResponse;
import com.f.backend.service.HotelService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import static org.springframework.http.HttpStatus.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            @RequestParam(value = "image") MultipartFile file

    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Hotel hotel = objectMapper.readValue(hotelJson, Hotel.class);
        try {
            hotelService.saveHotel(hotel, file);
            Map<String, String> response = new HashMap<>();
            response.put("Message ", "Hotel Added Successfully!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Message", "Hotel add failed!");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
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
        List<Hotel> hotels = hotelService.findHotelByLocationName(locationName);
        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/h/searchhotelname")
    public ResponseEntity<Hotel> findHotelByName(@RequestParam(value = "name") String name) {
        Hotel hotel = hotelService.findHotelByName(name);
        return ResponseEntity.ok(hotel);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Hotel> updateHotel(
            @PathVariable int id,
            @RequestPart Hotel hotel,
            @RequestParam(value = "image", required = true) MultipartFile file) throws IOException {
        Hotel updateHotel = hotelService.updateHotel(id, hotel, file);
        return ResponseEntity.ok(updateHotel);

    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAllHotel() {
        try {
            List<Hotel> allHotels = hotelService.getAllHotel();
            return ResponseEntity.status(OK).body(new ApiResponse("All Hotel Data", allHotels));
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }

    }

}
