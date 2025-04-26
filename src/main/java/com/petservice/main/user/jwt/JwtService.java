package com.petservice.main.user.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtService {

  private SecretKey secretKey;

  @Value("${app.jwt.access-token.expiration}")
  private Long accessTokenDurationMs;

  @Value("${app.jwt.refresh-token.expiration}")
  private Long refreshTokenDurationMs;

  public JwtService(@Value("${spring.jwt.secret}") String secret) {
    secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
        Jwts.SIG.HS256.key().build().getAlgorithm());
  }

  public String getLoginId(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
        .get("userLoginId", String.class);
  }

  public String getRole(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
        .get("role", String.class);
  }

  public String getUsername(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
        .get("username", String.class);
  }

  public String getTokenType(String token){
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
      .get("tokenType", String.class);
  }

  public Boolean isExpired(String token) {
    try {
      Jwts.parser()
          .verifyWith(secretKey)
          .build()
          .parseSignedClaims(token)
          .getPayload();
      return false;
    } catch (ExpiredJwtException e) {
      return true;
    } catch (JwtException e) {
      return true;
    }
  }

  public String createAccessToken(String LoginId, String UserName, String role) {
    return createJwt(LoginId, role, UserName, accessTokenDurationMs, "ACCESS");
  }

  public String createRefreshToken(String LoginId, String UserName, String role) {
    return createJwt(LoginId, role, UserName, refreshTokenDurationMs, "REFRESH");
  }

  public String createJwt(String userLoginId, String role, String UserName, Long expiredMs, String tokenType) {
    return Jwts.builder().claim("userLoginId", userLoginId).claim("role", role).claim("username",UserName)
        .claim("tokenType", tokenType).issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + expiredMs)).signWith(secretKey).compact();
  }

}
