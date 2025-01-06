package com.f.backend.reposiotry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.f.backend.entity.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location,Long>{
    
}
