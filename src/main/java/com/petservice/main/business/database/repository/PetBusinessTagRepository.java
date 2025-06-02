package com.petservice.main.business.database.repository;

import com.petservice.main.business.database.entity.PetBusinessTag;
import com.petservice.main.business.database.entity.TagType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetBusinessTagRepository extends JpaRepository<PetBusinessTag, Long> {
  List<PetBusinessTag> findByPetBusiness_Id(Long id);

  boolean existsByTagNameAndTagTypeAndPetBusiness_IdAndPetBusiness_BusinessName(String tagName,
      TagType tagType, Long id, String businessName);
}
