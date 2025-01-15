package com.f.backend.reposiotry;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.f.backend.entity.User;

public interface UserRepository extends JpaRepository<User,Integer>{
    
Optional<User> findByEmail(String email);
}
