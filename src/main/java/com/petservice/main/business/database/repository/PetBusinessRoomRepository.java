package com.petservice.main.business.database.repository;

import com.petservice.main.business.database.entity.PetBusinessRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetBusinessRoomRepository extends JpaRepository<PetBusinessRoom, Long> {

  PetBusinessRoom findByRoomTypeAndPetBusiness_RegistrationNumber(String roomType,
      String registrationNumber);

  PetBusinessRoom findByIdAndPetBusiness_Id(Long id, Long id1);

  List<PetBusinessRoom> findByPetBusiness_RegistrationNumber(String registrationNumber);

  List<PetBusinessRoom> findByPetBusiness_Id(Long id);

  boolean existsByRoomTypeAndPetBusiness_Id(String roomType, Long id);

  PetBusinessRoom findByPetBusiness_IdAndRoomType(Long id, String roomType);
}
