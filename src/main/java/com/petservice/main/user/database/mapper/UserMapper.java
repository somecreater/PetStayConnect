package com.petservice.main.user.database.mapper;

import com.petservice.main.business.database.mapper.PetBusinessMapper;
import com.petservice.main.user.database.dto.UserDTO;
import com.petservice.main.user.database.entity.User;
import com.petservice.main.user.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

  private final UserRepository userRepository;

  private final PetMapper petMapper;
  private final PetBusinessMapper petBusinessMapper;
  private final BookmarkMapper bookmarkMapper;
  public User toEntity(UserDTO userDTO){
    User user=new User();
    user.setId(userDTO.getId());
    user.setUserLoginId(userDTO.getUserLoginId());
    user.setEmail(userDTO.getEmail());
    user.setRole(userDTO.getRole());
    user.setName(userDTO.getName());
    user.setPhone(userDTO.getPhone());
    user.setLoginType(userDTO.getLoginType());
    user.setPetNumber(userDTO.getPetNumber());
    user.setCreatedAt(userDTO.getCreateAt());
    user.setUpdatedAt(userDTO.getUpdateAt());
    if(userDTO.getPetBusinessDTO()!=null) {
      user.setPetBusiness(petBusinessMapper.toEntity(userDTO.getPetBusinessDTO()));
    }
    if(userDTO.getPetDTOList() != null){
      user.setPetList(userDTO.getPetDTOList().stream()
          .map(petMapper::toEntity).toList());
    }
    if(userDTO.getBookmarkDTOList() != null){
      user.setBookmarkList(userDTO.getBookmarkDTOList().stream()
          .map(bookmarkMapper::toEntity).toList());
    }
    return user;
  }

  public UserDTO toDTO(User user){
    UserDTO userdto=new UserDTO();
    userdto.setId(user.getId());
    userdto.setUserLoginId(user.getUserLoginId());
    userdto.setName(user.getName());
    userdto.setEmail(user.getEmail());
    userdto.setRole(user.getRole());
    userdto.setPhone(user.getPhone());
    userdto.setLoginType(user.getLoginType());
    userdto.setPetNumber(user.getPetNumber());
    userdto.setCreateAt(user.getCreatedAt());
    userdto.setUpdateAt(user.getUpdatedAt());
    if(user.getPetBusiness()!=null) {
      userdto.setPetBusinessDTO(petBusinessMapper.toDTO(user.getPetBusiness()));
    }
    if(user.getPetList() != null){
      userdto.setPetDTOList(user.getPetList().stream()
          .map(petMapper::toDTO).toList());
    }
    if(user.getBookmarkList() != null){
      userdto.setBookmarkDTOList(user.getBookmarkList().stream()
          .map(bookmarkMapper::toDTO).toList());
    }
    return userdto;
  }
}
