package com.petservice.main.business.service;

import com.petservice.main.business.database.dto.PetBusinessRoomDTO;
import com.petservice.main.business.database.mapper.PetBusinessRoomMapper;
import com.petservice.main.business.database.repository.PetBusinessRoomRepository;
import com.petservice.main.business.service.Interface.PetBusinessRoomServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetBusinessRoomService implements PetBusinessRoomServiceInterface {

  private final PetBusinessRoomRepository petBusinessRoomRepository;

  private final PetBusinessRoomMapper petBusinessRoomMapper;

  @Override
  @Transactional(readOnly = true)
  public List<PetBusinessRoomDTO> getRoomByBusinessRegisterNumber(String RegisterNumber) {
    return petBusinessRoomRepository.findByPetBusiness_RegistrationNumber(RegisterNumber)
        .stream().map(petBusinessRoomMapper::toBasicDTO).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<PetBusinessRoomDTO> getRoomByBusinessId(Long business_id) {
    return petBusinessRoomRepository.findByPetBusiness_Id(business_id)
        .stream().map(petBusinessRoomMapper::toBasicDTO).toList();
  }
}
