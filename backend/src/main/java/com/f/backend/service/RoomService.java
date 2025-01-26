package com.f.backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.f.backend.entity.Hotel;
import com.f.backend.entity.Room;
import com.f.backend.reposiotry.HotelRepository;
import com.f.backend.reposiotry.RoomRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class RoomService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Value("${image.upload.dir}")
    private String uploadDir;

    public List<Room> getAllRoom() {
        return roomRepository.findAll();
    }

    public void saveRoom(Room room, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageFileName = saveImage(imageFile, room);
            room.setImage(imageFileName);
        }
        roomRepository.save(room);
    }

    public Room getRoomBId(int id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with this id: " + id));
    }

    public void deleteRoomById(int id) {
        if (!roomRepository.existsById(id)) {
            throw new EntityNotFoundException("Room not found with this id: " + id);
        }
        roomRepository.deleteById(id);
    }

    public Room updateRoom(int id, Room updateRoom, MultipartFile image) throws IOException {
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room Not found with this id: " + id));
        existingRoom.setName(updateRoom.getName());
        existingRoom.setPrice(updateRoom.getPrice());
        existingRoom.setArea(updateRoom.getArea());
        existingRoom.setAdultNo(updateRoom.getAdultNo());
        existingRoom.setChildNo(updateRoom.getChildNo());

        /**
         * Update the Location of the room
         */
        Hotel hotel = hotelRepository.findById(updateRoom.getHotel().getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Hotel not found with this id: " + updateRoom.getHotel().getId()));

        existingRoom.setHotel(hotel);

        /**
         * Update the image of the Room
         */

        if (image != null && !image.isEmpty()) {
            String fileName = saveImage(image, existingRoom);
            existingRoom.setImage(fileName);
        }
        return roomRepository.save(existingRoom);
    }

    public List<Room> findRoomByHotelName(String hotelName) {
        return roomRepository.findRoomByHotelName(hotelName);
    }

    public List<Room> findRoomByHotelId(int hotelId) {
        return roomRepository.findRoomByHotelId(hotelId);
    }

    public String saveImage(MultipartFile image, Room room) throws IOException {

        Path uploadPath = Paths.get(uploadDir + "/rooms");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileName = room.getName() + "_" + UUID.randomUUID().toString();

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(image.getInputStream(), filePath);
        return fileName;

    }

    public Room findRoomById(int id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room Not Found with this id: " + id));
    }

    public List<Room> getRoomByHotelId(int id){
        
        return getAllRoom().stream()
        .filter(room->room.getHotel().getId()==id).toList();
    
    }
}
