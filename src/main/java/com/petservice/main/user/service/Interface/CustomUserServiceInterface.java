package com.petservice.main.user.service.Interface;

import com.petservice.main.user.database.dto.CustomUserDetails;
import com.petservice.main.user.database.dto.UserDTO;
import com.petservice.main.user.database.entity.User;

public interface CustomUserServiceInterface {
  public UserDTO registerUser(UserDTO userDTO);
  public UserDTO UserLogin(String UserLoginId, String Password);
  public UserDTO getUserFromPrincipal(CustomUserDetails userDetails);
  public UserDTO getUserByEmail(String email);
  public UserDTO getUserByLoginId(String LoginId);
  public UserDTO UpdateUser(CustomUserDetails userDetails, UserDTO userDTO);
  public boolean DeleteUser(CustomUserDetails userDetails, String UserLoginId, String Password);
  public boolean UpdatePassword(String UserLoginId, String Password, String newPassword);
  public boolean UserValidation(UserDTO userDTO);
  public boolean isBlank(String str);

}
