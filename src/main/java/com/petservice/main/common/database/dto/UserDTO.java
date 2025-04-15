package com.petservice.main.common.database.dto;

import com.petservice.main.common.database.entity.Role;
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
