package com.petservice.main.api.database.repository;

import com.petservice.main.api.database.entity.BnsVLRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BnsVLRequestRepository extends JpaRepository<BnsVLRequestEntity, Long> {

  Optional<BnsVLRequestEntity> findByBno(String bNo);

  @Query("SELECT r FROM BnsVLRequestEntity r WHERE r.status = 'NONE' ORDER BY r.id ASC LIMIT 100")
  List<BnsVLRequestEntity> getNoneRequestList();

  @Query("SELECT r FROM BnsVLRequestEntity r WHERE r.status = 'PENDING' ORDER BY r.id ASC LIMIT 100")
  List<BnsVLRequestEntity> findByStatus_PENDING();

  @Query("SELECT r FROM BnsVLRequestEntity r WHERE r.status = 'ERROR' ORDER BY r.id ASC LIMIT 100")
  List<BnsVLRequestEntity> findByStatus_ERROR();

  @Query("SELECT r FROM BnsVLRequestEntity r WHERE r.status = 'NONE' ORDER BY r.id ASC LIMIT 100")
  List<BnsVLRequestEntity> findByStatus_NONE();

  long deleteByBno(String bno);
}
