package com.petservice.main.payment.database.repository;

import com.petservice.main.payment.database.entity.Payment;
import com.petservice.main.user.database.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {


  Page<Payment> findByReservation_PetBusiness_Id(Long businessId, Pageable pageable);
  Page<Payment> findByReservation_User_UserLoginId(String userLoginId, Pageable pageable);

  boolean existsByReservation_IdOrImpUidOrMerchantUid(Long id, String impUid, String merchantUid);
  Optional<Payment> findByImpUidAndMerchantUid(String impUid, String merchantUid);
  Optional<Payment> findByImpUid(String impUid);
}
