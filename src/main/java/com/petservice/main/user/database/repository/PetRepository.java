package com.petservice.main.user.database.repository;

import com.petservice.main.user.database.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
  List<Pet> findByUser_UserLoginId(String userLoginId);
}
