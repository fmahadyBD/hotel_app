package com.f.backend.reposiotry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.f.backend.entity.Room;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    @Query("Select r from Room r where r.hotel.name=:hotelName")
    public List<Room> findRoomByHotelName(@Param("hotelName") String hotelName);

    @Query("Select r from Room r where r.hotel.id=:hotelId")
    public List<Room> findRoomByHotelId(@Param("hotelId") int hotelId);
}
