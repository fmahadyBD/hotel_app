package com.f.backend.reposiotry;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.f.backend.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>{
     Optional<User> findByEmail(String email);
}
