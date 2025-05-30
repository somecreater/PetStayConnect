package com.petservice.main.common.config;

import com.petservice.main.common.database.entity.CatBreed;
import com.petservice.main.common.database.entity.DogBreed;
import com.petservice.main.common.database.repository.CatBreedRepository;
import com.petservice.main.common.database.repository.DogBreedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class InitData implements ApplicationRunner {

  private final CatBreedRepository catRepo;
  private final DogBreedRepository dogRepo;

  @Value("${app.sql.filepath}")
  private String SqlFilePath;
  @Value("${app.sql.local}")
  private String Local;

  @Value("${cat.api.url}") private String cat_baseUrl;
  @Value("${cat.api.key}") private String cat_apiKey;
  @Value("${dog.api.url}") private String dog_baseUrl;
  @Value("${dog.api.key}") private String dog_apiKey;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    WebClient Cat_breedClient= WebClient.builder()
        .baseUrl(cat_baseUrl)
        .defaultHeader("x-api-key", cat_apiKey)
        .build();

    WebClient Dog_breedClient=WebClient.builder()
        .baseUrl(dog_baseUrl)
        .defaultHeader("x-api-key", dog_apiKey)
        .build();

    if(catRepo.count() == 0){
      List<CatBreed> cats = Cat_breedClient.get()
          .retrieve()
          .bodyToFlux(CatBreed.class)
          .collectList()
          .block();
      catRepo.saveAll(cats);
    }

    if(dogRepo.count() == 0){
      List<DogBreed> dogs = Dog_breedClient.get()
          .retrieve()
          .bodyToFlux(DogBreed.class)
          .collectList()
          .block();

      dogRepo.saveAll(dogs);
    }
  }
}
