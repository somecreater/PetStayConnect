package com.petservice.main.business.database.repository;


import com.petservice.main.business.database.entity.PetBusiness;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetBusinessRepository extends JpaRepository<PetBusiness,Long> {

  PetBusiness findByUser_Id(Long id);

  boolean existsByPetBusinessType_Id(Long id);

}
