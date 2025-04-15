package com.petservice.main.user.service.Interface;

import com.petservice.main.common.database.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenServiceInterface {
  public Optional<RefreshToken> findByToken(String token);
  public RefreshToken createRefreshToken(String username);
  public RefreshToken verifyExpiration(RefreshToken token);
  public void deleteByUserId(String username);
}
