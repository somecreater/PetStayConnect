package com.petservice.main.user.jwt;

import com.petservice.main.user.database.dto.CustomUserDetails;
import com.petservice.main.user.database.entity.RefreshToken;
import com.petservice.main.user.database.entity.User;
import com.petservice.main.user.service.Interface.RefreshTokenServiceInterface;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

//JWT 토큰의 유효성 검사
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final RefreshTokenServiceInterface refreshTokenService;
  private static final long ACCESS_TOKEN_MAX_AGE_SECONDS = 15 * 60L;

  private static final String[] WITHOUT_TOKEN_URL={
      "/user/login",
      "/user/register",
      "/api/user/login",
      "/api/user/register",
  };

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    // API 경로(/api/) 가 아니면 필터를 적용하지 않음
    return !request.getRequestURI().startsWith("/api/");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
    FilterChain filterChain) throws ServletException, IOException {

    try {
      for (String url : WITHOUT_TOKEN_URL) {
        if (request.getRequestURI().compareTo(url) == 0) {
          filterChain.doFilter(request, response);
          return;
        }
      }

      String accessToken  = extractCookie(request, "accessToken");;

      if(accessToken ==null) {
        String hdr  = request.getHeader("Authorization");
          if (StringUtils.hasText(hdr ) && hdr.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
          }
      }

      if (accessToken == null) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }

      if(jwtService.isExpired(accessToken)){
        log.info("Access token expired; attempting refresh inside filter");

        String refreshToken = extractCookie(request, "refreshToken");
        if (refreshToken == null) {
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          return;
        }

        Optional<RefreshToken> findRefreshToken = refreshTokenService.findByToken(refreshToken);
        if (findRefreshToken.isEmpty() || refreshTokenService.verifyExpiration(findRefreshToken.get()) == null) {
          response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
          return;
        }

        User user = findRefreshToken.get().getUser();
        String newAccessToken =
            jwtService.createJwt(user.getUserLoginId(), user.getRole().name(), user.getName(),
                ACCESS_TOKEN_MAX_AGE_SECONDS * 1000, "ACCESS");

        ResponseCookie cookie = ResponseCookie.from("accessToken", newAccessToken)
            .httpOnly(true)
            .secure(true)
            .path("/")
            //.maxAge(ACCESS_TOKEN_MAX_AGE_SECONDS)
            .sameSite("None")
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        accessToken = newAccessToken;
      }

      if(!jwtService.isExpired(accessToken)) {
        String LoginId = jwtService.getLoginId(accessToken);
        String role = jwtService.getRole(accessToken);
        String Username = jwtService.getUsername(accessToken);

        CustomUserDetails customUserDetails = new CustomUserDetails(LoginId, Username, role);

        Authentication authentication =
            new UsernamePasswordAuthenticationToken(customUserDetails, null,
                customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("token auth");

      }else{
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        log.info("new token fail");
        return;
      }
    }catch (Exception e){
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
      log.error("Error JWT {}",e.getMessage());
      return;
    }

    filterChain.doFilter(request, response);

  }

  private String extractCookie(HttpServletRequest req, String name) {
    if (req.getCookies() == null) return null;
    for (Cookie c : req.getCookies()) {
      if (name.equals(c.getName())) {
        return c.getValue();
      }
    }
    return null;
  }
}
