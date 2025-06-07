package com.petservice.main.user.database.repository;

import com.petservice.main.user.database.entity.Role;
import com.petservice.main.user.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

  @Query("SELECT u FROM User u LEFT JOIN FETCH u.petBusiness WHERE u.userLoginId = :loginId")
  Optional<User> findByUserLoginIdWithBusiness(@Param("loginId") String loginId);

  Optional<User> findByUserLoginId(String userLoginId);

  @Modifying
  long deleteByUserLoginId(String userLoginId);

  User findByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByPhone(String phone);

  User findByName(String name);

  boolean existsByRole(Role role);
}
