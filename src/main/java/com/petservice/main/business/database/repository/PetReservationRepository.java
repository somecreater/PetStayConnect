package com.petservice.main.business.database.repository;

import com.petservice.main.business.database.entity.PetReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetReservationRepository extends JpaRepository<PetReservation,Long> {

}
