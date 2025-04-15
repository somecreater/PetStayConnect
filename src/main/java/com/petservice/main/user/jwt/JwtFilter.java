package com.petservice.main.user.jwt;

import com.petservice.main.common.database.dto.CustomUserDetails;
import com.petservice.main.common.database.entity.User;
import com.petservice.main.common.database.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//JWT 토큰의 유효성 검사
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserRepository userRepository;
  private static final String[] WITHOUT_TOKEN_URL={
      "/api/user/login",
      "/api/user/register",
      "/api/user/refresh"
  };

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
    FilterChain filterChain) throws ServletException, IOException {

    try {
      for (String url : WITHOUT_TOKEN_URL) {
        if (request.getRequestURI().equals(url)) {
          filterChain.doFilter(request, response);
          return;
        }
      }

      String authorization = null;
      Cookie[] cookies = request.getCookies();
      if (cookies != null) {
        for (Cookie cookie : cookies) {
          if ("accessToken".equals(cookie.getName())) {
            authorization = cookie.getValue();
            log.info("Found access token in cookie.");
            break;
          }
        }
      }
      if(authorization==null) {
        String accessToken = request.getHeader("Authorization");
          if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer ")) {
            authorization = accessToken.substring(7);
          }
      }
      if (authorization == null) {
        log.info("No token found in request");
        filterChain.doFilter(request, response);
        return;
      }

      String token = authorization;

      if (jwtService.isExpired(token)) {
        log.info("Token is expired");
        filterChain.doFilter(request, response);
        return;
      }

      String username = jwtService.getUsername(token);
      User user = userRepository.findByUserLoginId(username).orElse(null);

      if (user == null) {
        log.info("User is null");
        filterChain.doFilter(request, response);
        return;
      }

      CustomUserDetails customUserDetails = new CustomUserDetails(user);

      Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null,
              customUserDetails.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }catch (Exception e){
      log.error("Error JWT {}",e.getMessage());
    }
    filterChain.doFilter(request, response);

  }

}
