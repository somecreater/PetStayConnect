package com.petservice.main.common.database.mapper;

import com.petservice.main.common.database.dto.RefreshTokenDTO;
import com.petservice.main.common.database.entity.RefreshToken;
import com.petservice.main.common.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenMapper {

  private final UserRepository userRepository;

  public RefreshToken toEntity(RefreshTokenDTO dto){
    RefreshToken refreshToken=new RefreshToken();
    refreshToken.setId(dto.getId());
    refreshToken.setToken(dto.getToken());
    refreshToken.setExpiryDate(dto.getExpiryDate());
    refreshToken.setUser(userRepository.findByUserLoginId(dto.getUserLoginId()).get());
    return  refreshToken;
  }

  public RefreshTokenDTO toDTO(RefreshToken entity){
    RefreshTokenDTO refreshTokenDTO=new RefreshTokenDTO();
    refreshTokenDTO.setId(entity.getId());
    refreshTokenDTO.setToken(entity.getToken());
    refreshTokenDTO.setExpiryDate(entity.getExpiryDate());
    refreshTokenDTO.setUserId(entity.getUser()!=null ? entity.getUser().getId():null);
    refreshTokenDTO.setUserLoginId(entity.getUser()!=null ?entity.getUser().getUserLoginId():null);
    return refreshTokenDTO;
  }

}
