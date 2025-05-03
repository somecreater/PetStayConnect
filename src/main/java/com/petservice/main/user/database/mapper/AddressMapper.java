package com.petservice.main.user.database.mapper;

import com.petservice.main.user.database.dto.AddressDTO;
import com.petservice.main.user.database.entity.Address;
import com.petservice.main.user.database.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressMapper {
  private final AddressRepository addressRepository;

  public Address toEntity(AddressDTO addressDTO){

    Address address=new Address();
    address.setId(addressDTO.getId());
    address.setUserId(addressDTO.getUserId());
    address.setCorX(addressDTO.getCorX());
    address.setCorY(addressDTO.getCorY());
    address.setRoadAddress(addressDTO.getRoadAddress());
    address.setDetailAddress(addressDTO.getDetailAddress());
    address.setPostalAddress(addressDTO.getPostalAddress());
    address.setProvince(addressDTO.getProvince());
    address.setCity(addressDTO.getCity());
    address.setTown(addressDTO.getTown());

    return address;
  }

  public AddressDTO toDTO(Address address){

    AddressDTO addressDTO=new AddressDTO();
    addressDTO.setId(address.getId());
    addressDTO.setUserId(address.getUserId());
    addressDTO.setCorX(address.getCorX());
    addressDTO.setCorY(address.getCorY());
    addressDTO.setRoadAddress(address.getRoadAddress());
    addressDTO.setDetailAddress(address.getDetailAddress());
    addressDTO.setPostalAddress(address.getPostalAddress());
    addressDTO.setProvince(address.getProvince());
    addressDTO.setCity(address.getCity());
    addressDTO.setTown(address.getTown());

    return addressDTO;
  }
}
