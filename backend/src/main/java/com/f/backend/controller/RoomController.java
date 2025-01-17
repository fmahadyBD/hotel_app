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

import com.f.backend.entity.Room;
import com.f.backend.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/")
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> rooms = roomService.getAllRoom();
        return ResponseEntity.ok(rooms);
    }

    /**
     * @param r
     * @param file
     * @return
     * @throws IOException
     *
     *                     Post: URL
     *                     form-data
     *                     key value Content type
     *                     r json data application/json
     *                     image give the file multipart/form-data
     */

    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> saveRoom(
            @RequestPart(value = "room") String roomJson,
            @RequestParam(value = "image", required = true) MultipartFile file) throws IOException {

        Map<String, String> response = new HashMap<>();

        if (roomJson == null || roomJson.isEmpty()) {
            response.put("message", "Invalid data");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (file == null || file.isEmpty()) {
            response.put("message", "Invaid image or Image file is misssing");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Room room = objectMapper.readValue(roomJson, Room.class);

        try {
            roomService.saveRoom(room, file);
            response.put("message", "Room Added Successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {

            response.put("message", "Unexpected error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable int id) {
        try {
            Room room = roomService.findRoomById(id);
            return ResponseEntity.ok(room);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Room> updateRoom(
            @PathVariable int id,
            @RequestPart Room room,
            @RequestParam(value = "image", required = true) MultipartFile file) throws IOException {
        Room updateRoom = roomService.updateRoom(id, room, file);
        return ResponseEntity.ok(updateRoom);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteRoom(@PathVariable int id) {

        Map<String, String> response = new HashMap<>();

        try {
            roomService.deleteRoomById(id);
            response.put("message", "Room Deleted Successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (EntityNotFoundException e) {
            response.put("message", "Not entity exist with this id" + id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            response.put("message", "Unexpected error to delete Room");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    @GetMapping("/r/findRoomByHotelId")
    public ResponseEntity<List<Room>> findRoomByHotelId(@RequestParam("hotelId") int hotelId) {
        List<Room> rooms = roomService.findRoomByHotelId(hotelId);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/r/findRoomByHotelName")
    public ResponseEntity<List<Room>> findRoomByHotelName(@RequestParam("hotelName") String hotelName) {
        List<Room> rooms = roomService.findRoomByHotelName(hotelName);
        return ResponseEntity.ok(rooms);
    }

}
