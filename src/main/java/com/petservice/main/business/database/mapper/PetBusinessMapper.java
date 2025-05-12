package com.petservice.main.business.database.mapper;


import com.petservice.main.business.database.dto.PetBusinessDTO;
import com.petservice.main.business.database.entity.PetBusiness;
import com.petservice.main.business.database.repository.PetBusinessTypeRepository;
import com.petservice.main.user.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PetBusinessMapper {

  private final UserRepository userRepository;
  private final PetBusinessTypeRepository petBusinessTypeRepository;

  private final PetBusinessRoomMapper petBusinessRoomMapper;
  private final ReservationMapper reservationMapper;

  public PetBusiness toEntity(PetBusinessDTO petBusinessDTO){

    PetBusiness petBusiness=new PetBusiness();
    petBusiness.setId(petBusinessDTO.getId());
    petBusiness.setBusinessName(petBusinessDTO.getBusinessName());
    petBusiness.setStatus(petBusinessDTO.getStatus());
    petBusiness.setMinPrice(petBusinessDTO.getMinPrice());
    petBusiness.setMaxPrice(petBusinessDTO.getMaxPrice());
    petBusiness.setFacilities(petBusinessDTO.getFacilities());
    petBusiness.setDescription(petBusinessDTO.getDescription());
    petBusiness.setAvgRate(petBusinessDTO.getAvgRate());
    petBusiness.setRegistrationNumber(petBusinessDTO.getRegistrationNumber());
    petBusiness.setBankAccount(petBusinessDTO.getBankAccount());
    petBusiness.setVarification(petBusinessDTO.getVarification());
    petBusiness.setProvince(petBusinessDTO.getProvince());
    petBusiness.setCity(petBusinessDTO.getCity());
    petBusiness.setTown(petBusinessDTO.getTown());
    petBusiness.setCreatedAt(petBusinessDTO.getCreatedAt());
    petBusiness.setUpdatedAt(petBusinessDTO.getUpdatedAt());
    if(petBusinessDTO.getUserId()!=null) {
      petBusiness.setUser(
          userRepository.findById(
              petBusinessDTO.getUserId()).orElse(null));
    }
    if(petBusinessDTO.getPetBusinessTypeId()!=null){
      petBusiness.setPetBusinessType(
          petBusinessTypeRepository.findById(
              petBusinessDTO.getPetBusinessTypeId()).orElse(null));
    }
    if(petBusinessDTO.getPetBusinessRoomDTOList()!=null){
      petBusiness.setPetBusinessRoomList(petBusinessDTO.getPetBusinessRoomDTOList().stream()
          .map(petBusinessRoomMapper::toEntity).toList());
    }
    if(petBusinessDTO.getReservationDTOList()!=null){
      petBusiness.setReservationList(petBusinessDTO.getReservationDTOList().stream()
          .map(reservationMapper::toEntity).toList());
    }
    return petBusiness;
  }

  public PetBusinessDTO toDTO(PetBusiness petBusiness){

    PetBusinessDTO petBusinessDTO =new PetBusinessDTO();
    petBusinessDTO.setId(petBusiness.getId());
    petBusinessDTO.setBusinessName(petBusiness.getBusinessName());
    petBusinessDTO.setStatus(petBusiness.getStatus());
    petBusinessDTO.setMinPrice(petBusiness.getMinPrice());
    petBusinessDTO.setMaxPrice(petBusiness.getMaxPrice());
    petBusinessDTO.setFacilities(petBusiness.getFacilities());
    petBusinessDTO.setDescription(petBusiness.getDescription());
    petBusinessDTO.setAvgRate(petBusiness.getAvgRate());
    petBusinessDTO.setRegistrationNumber(petBusiness.getRegistrationNumber());
    petBusinessDTO.setBankAccount(petBusiness.getBankAccount());
    petBusinessDTO.setVarification(petBusiness.getVarification());
    petBusinessDTO.setProvince(petBusiness.getProvince());
    petBusinessDTO.setCity(petBusiness.getCity());
    petBusinessDTO.setTown(petBusiness.getTown());
    petBusinessDTO.setCreatedAt(petBusiness.getCreatedAt());
    petBusinessDTO.setUpdatedAt(petBusiness.getUpdatedAt());

    if(petBusiness.getUser()!=null){
      petBusinessDTO.setUserId(petBusiness.getUser().getId());
    }
    if(petBusiness.getPetBusinessType()!=null){
      petBusinessDTO.setPetBusinessTypeName(petBusiness.getPetBusinessType().getTypeName());
      petBusinessDTO.setPetBusinessTypeId(petBusiness.getPetBusinessType().getId());
    }
    if(petBusiness.getPetBusinessRoomList()!=null){
      petBusinessDTO.setPetBusinessRoomDTOList(petBusiness.getPetBusinessRoomList().stream()
          .map(petBusinessRoomMapper::toDTO).toList());
    }
    if(petBusiness.getReservationList()!=null){
      petBusinessDTO.setReservationDTOList(petBusiness.getReservationList().stream()
          .map(reservationMapper::toDTO).toList());
    }

    return petBusinessDTO;
  }

  public PetBusinessDTO toBasicDTO(PetBusiness petBusiness){

    PetBusinessDTO petBusinessDTO =new PetBusinessDTO();
    petBusinessDTO.setId(petBusiness.getId());
    petBusinessDTO.setBusinessName(petBusiness.getBusinessName());
    petBusinessDTO.setStatus(petBusiness.getStatus());
    petBusinessDTO.setMinPrice(petBusiness.getMinPrice());
    petBusinessDTO.setMaxPrice(petBusiness.getMaxPrice());
    petBusinessDTO.setFacilities(petBusiness.getFacilities());
    petBusinessDTO.setDescription(petBusiness.getDescription());
    petBusinessDTO.setAvgRate(petBusiness.getAvgRate());
    petBusinessDTO.setRegistrationNumber(petBusiness.getRegistrationNumber());
    petBusinessDTO.setBankAccount(petBusiness.getBankAccount());
    petBusinessDTO.setVarification(petBusiness.getVarification());
    petBusinessDTO.setProvince(petBusiness.getProvince());
    petBusinessDTO.setCity(petBusiness.getCity());
    petBusinessDTO.setTown(petBusiness.getTown());
    petBusinessDTO.setCreatedAt(petBusiness.getCreatedAt());
    petBusinessDTO.setUpdatedAt(petBusiness.getUpdatedAt());
    if(petBusiness.getPetBusinessType()!=null){
      petBusinessDTO.setPetBusinessTypeName(petBusiness.getPetBusinessType().getTypeName());
      petBusinessDTO.setPetBusinessTypeId(petBusiness.getPetBusinessType().getId());
    }

    return petBusinessDTO;
  }
}
