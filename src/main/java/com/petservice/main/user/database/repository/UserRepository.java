package com.petservice.main.user.database.repository;

import com.petservice.main.user.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
  Optional<User> findByUserLoginId(String userLoginId);
  @Modifying
  long deleteByUserLoginId(String userLoginId);

  User findByEmail(String email);
}
