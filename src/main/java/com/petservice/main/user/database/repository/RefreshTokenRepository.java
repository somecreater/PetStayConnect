package com.petservice.main.user.database.repository;

import com.petservice.main.user.database.entity.RefreshToken;
import com.petservice.main.user.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

  Optional<RefreshToken> findByToken(String token);

  RefreshToken findByUser_Id(Long User_id);

  @Modifying
  int deleteByUser(User user);
}
