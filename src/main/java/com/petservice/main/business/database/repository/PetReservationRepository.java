package com.petservice.main.business.database.repository;

import com.petservice.main.business.database.entity.PetReservation;
import com.petservice.main.business.database.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PetReservationRepository extends JpaRepository<PetReservation,Long> {

  @Modifying
  @Transactional
  @Query("delete from PetReservation pr " +
      "where pr.reservation.id = :reservationId")
  int deleteByReservationId(@Param("reservationId") Long reservationId);

  List<PetReservation> findByReservation_Id(Long id);

  List<PetReservation> findByPet_Id(Long id);

  long deleteByReservation_User_Id(Long id);

  long deleteByReservation_PetBusiness_Id(Long id);
}
