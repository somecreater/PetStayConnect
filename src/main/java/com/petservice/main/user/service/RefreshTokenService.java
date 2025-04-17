package com.petservice.main.user.service;

import com.petservice.main.user.database.entity.RefreshToken;
import com.petservice.main.user.database.entity.User;
import com.petservice.main.user.database.repository.RefreshTokenRepository;
import com.petservice.main.user.database.repository.UserRepository;
import com.petservice.main.user.service.Interface.RefreshTokenServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements RefreshTokenServiceInterface {


  @Value("${app.jwt.refresh-token.expiration}")
  private Long refreshTokenDurationMs;

  private final RefreshTokenRepository refreshTokenRepository;
  private final UserRepository userRepository;

  @Override
  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  @Override
  public RefreshToken createRefreshToken(String userLoginId) {
    User user=userRepository.findByUserLoginId(userLoginId)
        .orElseThrow(() -> new RuntimeException("User not found with LoginId: " + userLoginId));

    Optional<RefreshToken> existingToken = refreshTokenRepository.findByUser(user);
    if(existingToken.isPresent()){
      RefreshToken refreshToken = existingToken.get();
      refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
      return refreshTokenRepository.save(refreshToken);
    }

    RefreshToken refreshToken=new RefreshToken();
    refreshToken.setUser(user);
    refreshToken.setToken(UUID.randomUUID().toString());
    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));

    return refreshTokenRepository.save(refreshToken);
  }

  @Override
  public RefreshToken verifyExpiration(RefreshToken token) {
    if(token.getExpiryDate().compareTo(Instant.now()) < 0){
      refreshTokenRepository.delete(token);
      return null;
    }
    return token;
  }

  @Override
  @Transactional
  public void deleteByUserId(String userLoginId) {
    User user=userRepository.findByUserLoginId(userLoginId)
        .orElseThrow(() -> new RuntimeException("User not found with LoginId: " + userLoginId));
    refreshTokenRepository.deleteByUser(user);
  }
}
