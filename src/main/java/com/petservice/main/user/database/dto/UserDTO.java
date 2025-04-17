package com.petservice.main.user.database.dto;

import com.petservice.main.user.database.entity.Role;
import lombok.Data;

@Data
public class UserDTO {

  private Long id;
  private String userLoginId;
  private String password;
  private String email;
  private String name;
  private Role role;

}
