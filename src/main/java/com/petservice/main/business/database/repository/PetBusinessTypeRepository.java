package com.petservice.main.business.database.repository;

import com.petservice.main.business.database.entity.PetBusinessType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PetBusinessTypeRepository extends JpaRepository<PetBusinessType, Long> {

  boolean existsByTypeName(String typeName);

  boolean existsByTypeCode(String typeCode);

  @Query("SELECT p FROM PetBusinessType p WHERE p.typeCode LIKE %:code%")
  PetBusinessType findByTypeCodeLike(@Param("code")String code);

}
