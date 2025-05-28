package com.petservice.main.business.database.repository;

import com.petservice.main.business.database.entity.Reservation;
import com.petservice.main.business.database.entity.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {


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
  boolean existsByCheckInAndCheckOut(
      @Param("loginId")   String loginId,
      @Param("checkIn")   LocalDate checkIn,
      @Param("checkOut")  LocalDate checkOut,
      @Param("petId")     Long petId
  );


  boolean existsByPetBusinessRoom_IdAndPetBusiness_Id(Long id, Long id1);

  @Query(
      value = "SELECT r FROM Reservation r " +
      "WHERE r.status   = :status " +
      "  AND r.checkOut < :today " +
      "ORDER BY r.checkOut ASC",
      countQuery = "SELECT COUNT(r) FROM Reservation r " +
          "WHERE r.status   = :status " +
          "  AND r.checkOut < :today")
  List<Reservation> findByStatusAndExpired(
      @Param("status") ReservationStatus status,
      @Param("today") LocalDate today,
      Pageable pageable);

  @Query(
      value = "SELECT r FROM Reservation r " +
          "JOIN r.petBusiness pb " +
          "WHERE pb.id       = :businessId " +
          "  AND r.status   = :status " +
          "  AND r.checkOut < :today " +
          "ORDER BY r.checkOut ASC",
      countQuery = "SELECT COUNT(r) FROM Reservation r " +
          "JOIN r.petBusiness pb " +
          "WHERE pb.id       = :businessId " +
          "  AND r.status   = :status " +
          "  AND r.checkOut < :today"
  )
  List<Reservation> findByBusinessAndExpired(
      @Param("businessId") Long businessId,
      @Param("status")     ReservationStatus status,
      @Param("today")      LocalDate today,
      Pageable pageable);

  Page<Reservation> findByUser_UserLoginId(String userLoginId, Pageable pageable);

  Page<Reservation> findByPetBusiness_Id(Long PetBusiness_Id, Pageable pageable);

  long deleteByUser_Id(Long id);

  long deleteByPetBusiness_Id(Long id);

  List<Reservation> findByUser_Id(Long id);

  @Query("SELECT r FROM Reservation r JOIN r.petBusiness pb WHERE pb.id = :business_id")
  List<Reservation> findByBusiness(@Param("business_id") Long business_id);

  @Modifying
  @Query("UPDATE Reservation r SET r.user = null WHERE r.user.id = :userId")
  int nullifyUserReference(@Param("userId") Long userId);

  @Modifying
  @Query("UPDATE Reservation r SET r.petBusiness = null WHERE r.petBusiness.id = :businessId")
  int nullifyBusinessReference(@Param("businessId") Long businessId);
}
