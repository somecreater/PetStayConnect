package com.petservice.main.api.database.repository;

import com.petservice.main.api.database.entity.BnsVLRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BnsVLRequestRepository extends JpaRepository<BnsVLRequestEntity, Long> {

  BnsVLRequestEntity findByBNo(String bNo);

  @Query("SELECT r FROM BnsVLRequestEntity r WHERE r.status = 'NONE' ORDER BY r.id ASC LIMIT 100")
  List<BnsVLRequestEntity> getNoneRequestList();
}
