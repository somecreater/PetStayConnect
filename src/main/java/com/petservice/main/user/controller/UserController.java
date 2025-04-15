package com.petservice.main.user.controller;

import com.petservice.main.common.database.dto.*;
import com.petservice.main.common.database.entity.RefreshToken;
import com.petservice.main.common.database.entity.User;
import com.petservice.main.common.database.mapper.UserMapper;
import com.petservice.main.user.jwt.JwtService;
import com.petservice.main.user.service.Interface.CustomUserServiceInterface;
import com.petservice.main.user.service.Interface.RefreshTokenServiceInterface;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

  private final JwtService jwtService;
  private final RefreshTokenServiceInterface refreshTokenService;
  private final CustomUserServiceInterface customUserService;
  private final UserDetailsService userDetailsService;

  private final UserMapper userMapper;

  private static final Long JWT_EXPIRATION = 1000L * 60 * 60;

  //토큰 리프레시
  @PostMapping("/refresh")
  public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest refreshRequest,
      HttpServletResponse response) {
    Map<String, Object> result = new HashMap<>();

    String tokenInfo = refreshRequest.getRefreshToken();
    Optional<RefreshToken> findToken = refreshTokenService.findByToken(tokenInfo);
    if (findToken.isEmpty()) {
      result.put("result", false);
      result.put("message", "토큰이 존재하지 않습니다.");
      return ResponseEntity.ok(result);
    }

    RefreshToken refreshToken = findToken.get();
    RefreshToken verifiedToken = refreshTokenService.verifyExpiration(refreshToken);
    if (verifiedToken == null) {
      result.put("result", false);
      result.put("message", "토큰이 이미 만료되었습니다.");
      return ResponseEntity.ok(result);
    }
    try {
      User user = verifiedToken.getUser();
      String newAccessToken =
          jwtService.createJwt(user.getUserLoginId(), user.getRole().name(), JWT_EXPIRATION,
              "ACCESS");

      ResponseCookie.ResponseCookieBuilder token =
          ResponseCookie.from("accessToken", newAccessToken);
      token.httpOnly(true);
      token.secure(true);
      token.path("/");
      token.maxAge(JWT_EXPIRATION / 1000);
      token.sameSite("Strict");
      ResponseCookie accessCookie = token.build();

      response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());

      return ResponseEntity.ok(new TokenRefreshResponse(newAccessToken, tokenInfo, "Bearer"));
    }catch (Exception e){
      log.error("Token Refresh Error: {}",e.getMessage());
      result.put("result", false);
      result.put("message","토큰 생성도중 오류 발생");
      return ResponseEntity.ok(result);
    }
  }

  //회원 로그인
  @PostMapping("/login")
  public ResponseEntity<?> AuthenticateUser(@RequestBody LoginRequest loginRequest,
      HttpServletResponse response) {
    try {
      String userLoginId = loginRequest.getUsername();
      String userPassword = loginRequest.getPassword();

      log.info(userLoginId);
      log.info(userPassword);

      User user = customUserService.UserLogin(userLoginId, userPassword);
      String accessToken = jwtService.createAccessToken(user.getUserLoginId(), user.getRole().name());
      RefreshToken refreshTokenEntity = refreshTokenService.createRefreshToken(user.getUserLoginId());
      String refreshToken = refreshTokenEntity.getToken();

      ResponseCookie.ResponseCookieBuilder token =
          ResponseCookie.from("accessToken", accessToken);
      token.httpOnly(true);
      token.secure(true);
      token.path("/");
      token.maxAge(JWT_EXPIRATION / 1000);
      token.sameSite("Strict");
      ResponseCookie accessCookie = token.build();

      long refreshMaxAge = refreshTokenEntity.getExpiryDate().getEpochSecond() -
          (System.currentTimeMillis() / 1000);
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

      Map<String, Object> result = new HashMap<>();
      result.put("accessToken", accessToken);
      result.put("refreshToken", refreshToken);
      result.put("tokenType", "Bearer");
      result.put("authenticated", true);
      result.put("name", user.getName());
      result.put("LoginId", user.getUserLoginId());

      return ResponseEntity.status(HttpStatus.CREATED)
        .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
        .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
        .body(result);

    }catch (Exception e){
      log.error("Auth Fail: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("authenticated",false));
    }
  }

  //회원 로그아웃
  @PostMapping("/logout")
  public ResponseEntity<?> logoutUser(@AuthenticationPrincipal CustomUserDetails principal,
      HttpServletResponse response){

    Map<String, Object> result = new HashMap<>();
    try {

      String userLoginId = principal.getUsername();
      refreshTokenService.deleteByUserId(userLoginId);

      ResponseCookie deleteAccessCookie =
          ResponseCookie.from("accessToken", "").httpOnly(true).secure(true).path("/").maxAge(0)
              .sameSite("Strict").build();
      ResponseCookie deleteRefreshCookie =
          ResponseCookie.from("refreshToken", "").httpOnly(true).secure(true)
              .path("/api/user/refresh").maxAge(0).sameSite("Strict").build();

      response.addHeader(HttpHeaders.SET_COOKIE, deleteAccessCookie.toString());
      response.addHeader(HttpHeaders.SET_COOKIE, deleteRefreshCookie.toString());

      result.put("result",true);
      return ResponseEntity.ok(result);

    }catch (Exception e){
      log.error("로그아웃에 실패했습니다: {}", e.getMessage());
      result.put("result",false);
      return ResponseEntity.ok(result);
    }
  }

  //회원 등록
  @PostMapping("/register")
  public ResponseEntity<?> RegisterUser(@RequestBody UserDTO user){
    Map<String,Object> result=new HashMap<>();
    if(user==null){
      result.put("result",false);
      return ResponseEntity.ok(result);
    }

    User newuser= customUserService.registerUser(user);
    UserDTO newuserDto=userMapper.toDTO(newuser);

    result.put("result",true);
    result.put("UserInfo",newuserDto);

    return  ResponseEntity.ok(result);
  }

  //회원 탈퇴
  @PostMapping("/delete")
  public ResponseEntity<?> DeleteUser(
      @AuthenticationPrincipal CustomUserDetails principal,
      @RequestBody Map<String,String> deleteRequest){
    Map<String,Object> result=new HashMap<>();
    if(principal==null){
      result.put("auth",false);
      return ResponseEntity.ok(result);
    }

    return ResponseEntity.ok(result);
  }

  //회원 정보 읽기
  @GetMapping("/info")
  public ResponseEntity<?> getUserInfo(
      @AuthenticationPrincipal CustomUserDetails principal){
    Map<String,Object> result=new HashMap<>();
    if(principal==null){
      result.put("auth",false);
      return ResponseEntity.ok(result);
    }
    result.put("loginId",principal.getUsername());
    result.put("UserName",principal.getName());

    return ResponseEntity.ok(result);
  }

  //회원 세부정보 읽기
  @GetMapping("/detailInfo")
  public ResponseEntity<?> getUserDetailInfo(
      @AuthenticationPrincipal CustomUserDetails principal,
      @RequestParam(name = "userLoginId") String UserLoginId){
    Map<String,Object> result=new HashMap<>();
    if(principal==null||principal.getUsername().compareTo(UserLoginId)!=0){
      result.put("auth",false);
      return ResponseEntity.ok(result);
    }

    UserDTO userDetailInfo=userMapper
        .toDTO(customUserService.getUserFromPrincipal(principal));
    result.put("auth",true);
    result.put("userDetail",userDetailInfo);

    return ResponseEntity.ok(result);
  }
}
