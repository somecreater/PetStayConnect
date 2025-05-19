package com.petservice.main.business.database.repository;

import com.petservice.main.business.database.entity.Reservation;
import com.petservice.main.business.database.entity.ReservationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  List<Reservation> findByUser_UserLoginId(String userLoginId);

  /*
  특정 회원의 특정 펫이 특정 기간에 등록된 예약이 존재하는지 검사
  */
  @Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END" +
      " FROM Reservation r " +
      " JOIN r.user u" +
      " JOIN r.petReservationList pr" +
      " WHERE u.userLoginId = :loginId" +
      " AND pr.pet.id       = :petId" +
      " AND r.checkIn  < :checkOut" +
      " AND r.checkOut > :checkIn")
  boolean existsByCheckInAndCheckOut(String loginId, LocalDate checkIn, LocalDate checkOut,
      Long petId);

  List<Reservation> findByPetBusiness_Id(Long id);

  boolean existsByPetBusinessRoom_IdAndPetBusiness_Id(Long id, Long id1);

  @Query("SELECT r FROM Reservation r "
      + "WHERE r.status = :status "
      + "AND r.checkOut < :today "
      + "ORDER BY r.checkOut ASC")
  List<Reservation> findByStatusAndExpired(
      @Param("status") ReservationStatus status,
      @Param("today") LocalDate today,
      Pageable pageable);

  @Query("SELECT r FROM Reservation r "
      + "JOIN r.petBusiness pb "
      + "WHERE pb.id     = :businessId "
      + "AND r.status = :status "
      + "AND r.checkOut < :today "
      + "ORDER BY r.checkOut ASC")
  List<Reservation> findByBusinessAndExpired(
      @Param("businessId") Long businessId,
      @Param("status")     ReservationStatus status,
      @Param("today")      LocalDate today,
      Pageable pageable);
}
