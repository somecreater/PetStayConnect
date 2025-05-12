package com.petservice.main.user.service;

import com.petservice.main.user.database.dto.AddressDTO;
import com.petservice.main.user.database.entity.Address;
import com.petservice.main.user.database.mapper.AddressMapper;
import com.petservice.main.user.database.repository.AddressRepository;
import com.petservice.main.user.service.Interface.AddressServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService implements AddressServiceInterface {

  private final AddressRepository addressRepository;
  private final AddressMapper addressMapper;

  @Override
  public AddressDTO getAddressByUserId(Long UserId) {
    Address address = addressRepository.findByUserId(UserId);
    if(address==null){
      return null;
    }
    return addressMapper.toDTO(address);
  }


  public AddressDTO getAddressByUserLoginId(String UserLoginId){
    Address address = addressRepository.findByUserLoginId(UserLoginId);
    if(address==null){
      return null;
    }
    return addressMapper.toDTO(address);
  }
}
