package com.petservice.main.user.service.Interface;

import com.petservice.main.user.database.dto.AddressDTO;

public interface AddressServiceInterface {
  public AddressDTO getAddressByUserId(Long UserId);
  public AddressDTO getAddressByUserLoginId(String UserLoginId);
}
