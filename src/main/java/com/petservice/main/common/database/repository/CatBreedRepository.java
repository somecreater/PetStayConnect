package com.petservice.main.common.database.repository;

import com.petservice.main.common.database.entity.CatBreed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatBreedRepository  extends JpaRepository<CatBreed, String> {
}
