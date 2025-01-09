package com.f.backend.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.f.backend.entity.Room;
import com.f.backend.service.RoomService;

import java.io.IOException;
import java.util.List;

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
    public ResponseEntity<String> saveRoom(
            @RequestPart Room r,
            @RequestParam(value = "image", required = true) MultipartFile file) throws IOException {
        roomService.saveRoom(r, file);

        return new ResponseEntity<>("Room saved Successfully", HttpStatus.CREATED);

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
    public ResponseEntity<String> deleteRoom(@PathVariable int id) {

        try {
            roomService.deleteRoomById(id);
            return ResponseEntity.ok("Room with this ID " + id + " has been Deleted");

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

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
