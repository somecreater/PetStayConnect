package com.petservice.main.user.database.mapper;

import com.petservice.main.user.database.dto.UserDTO;
import com.petservice.main.user.database.entity.User;
import com.petservice.main.user.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

  private final UserRepository userRepository;

  public User toEntity(UserDTO userDTO){
    User user=new User();
    user.setId(userDTO.getId());
    user.setUserLoginId(userDTO.getUserLoginId());
    user.setEmail(userDTO.getEmail());
    user.setRole(userDTO.getRole());
    user.setName(userDTO.getName());
    return user;
  }

  public UserDTO toDTO(User user){
    UserDTO userdto=new UserDTO();
    userdto.setId(user.getId());
    userdto.setUserLoginId(user.getUserLoginId());
    userdto.setName(user.getName());
    userdto.setEmail(user.getEmail());
    userdto.setRole(user.getRole());
    return userdto;
  }
}
