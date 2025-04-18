package com.petservice.main.user.service.Interface;

import com.petservice.main.user.database.dto.CustomUserDetails;
import com.petservice.main.user.database.dto.UserDTO;
import com.petservice.main.user.database.entity.User;

public interface CustomUserServiceInterface {
  public User registerUser(UserDTO userDTO);
  public User UserLogin(String UserLoginId, String Password);
  public User getUserFromPrincipal(CustomUserDetails userDetails);
  public User getUserByEmail(String email);
  public User UpdateUser(CustomUserDetails userDetails, UserDTO userDTO);
  public boolean DeleteUser(CustomUserDetails userDetails, String UserLoginId, String Password);
  public boolean UserValidation(UserDTO userDTO);
}
