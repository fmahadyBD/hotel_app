package com.f.backend.reposiotry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.f.backend.entity.Hotel;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer> {

    @Query("SELECT h FROM Hotel h WHERE h.location.name = :locationName")
    List<Hotel> findHotelByLocationName(@Param("locationName") String locationName);
    Optional<Hotel> findHotelByName(String name);
}
