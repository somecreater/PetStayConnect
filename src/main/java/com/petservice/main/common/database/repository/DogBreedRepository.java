package com.petservice.main.common.database.repository;

import com.petservice.main.common.database.entity.DogBreed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogBreedRepository extends JpaRepository<DogBreed, Integer> {

}
