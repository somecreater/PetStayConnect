package com.petservice.main.user.service.Interface;

import com.petservice.main.common.database.dto.CustomUserDetails;
import com.petservice.main.common.database.dto.UserDTO;
import com.petservice.main.common.database.entity.User;

public interface CustomUserServiceInterface {
  public User registerUser(UserDTO userDTO);
  public User UserLogin(String UserLoginId, String Password);
  public User getUserFromPrincipal(CustomUserDetails userDetails);
  public boolean DeleteUser(CustomUserDetails userDetails, String UserLoginId, String Password);
}
