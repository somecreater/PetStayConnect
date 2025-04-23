package com.petservice.main.business.database.repository;

import com.petservice.main.business.database.entity.PetBusinessRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetBusinessRoomRepository extends JpaRepository<PetBusinessRoom, Long> {

}
