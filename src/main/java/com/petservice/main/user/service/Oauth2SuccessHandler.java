package com.petservice.main.user.service;

import com.petservice.main.common.database.entity.RefreshToken;
import com.petservice.main.user.jwt.JwtService;
import com.petservice.main.user.service.Interface.RefreshTokenServiceInterface;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class Oauth2SuccessHandler implements AuthenticationSuccessHandler {

  @Autowired
  private JwtService jwtService;
  @Autowired
  private RefreshTokenServiceInterface refreshTokenService;
  private final String redirect="/info";
  private static final Long JWT_EXPIRATION = 1000L * 60 * 60;

  //구글 로그인 성공 시 처리(헤더에 토큰들만 삽입)
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
    String email = oAuth2User.getAttribute("email");
    String name = oAuth2User.getAttribute("name");

    String accessToken=jwtService.createAccessToken(email,
      authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining()));
    RefreshToken refreshTokenEntity =refreshTokenService.createRefreshToken(email);
    String refreshToken= refreshTokenEntity.getToken();

    ResponseCookie.ResponseCookieBuilder token =
        ResponseCookie.from("accessToken", accessToken);
    token.httpOnly(true);
    token.secure(true);
    token.path("/");
    token.maxAge(JWT_EXPIRATION / 1000);
    token.sameSite("Strict");
    ResponseCookie accessCookie = token.build();

    long refreshMaxAge = refreshTokenEntity.getExpiryDate().getEpochSecond() - (System.currentTimeMillis() / 1000);
    ResponseCookie.ResponseCookieBuilder
        refreshCookieBuilder = ResponseCookie.from("refreshToken", refreshToken);
    refreshCookieBuilder.httpOnly(true);
    refreshCookieBuilder.secure(true);
    refreshCookieBuilder.path("/api/user/refresh");
    refreshCookieBuilder.maxAge(refreshMaxAge);
    refreshCookieBuilder.sameSite("Strict");
    ResponseCookie refreshCookie = refreshCookieBuilder.build();

    response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
    response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

    response.sendRedirect(redirect);
  }
}
