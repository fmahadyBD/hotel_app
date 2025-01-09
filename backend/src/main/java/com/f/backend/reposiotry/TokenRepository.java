package com.f.backend.reposiotry;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.f.backend.entity.Token;
@Repository
public interface TokenRepository extends JpaRepository<Token,Integer> {
 Optional<Token> findByToken(String  token);

    @Query("""
    Select t from Token t inner join User u on t.user.id= u.id
    where t.user.id= :userId and t.logout=false
""")
    List<Token> findAllTokenByUser(Integer userId);
    
} 