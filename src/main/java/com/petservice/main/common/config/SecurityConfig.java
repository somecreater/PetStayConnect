package com.petservice.main.common.config;

import com.petservice.main.user.jwt.JwtFilter;
import com.petservice.main.user.jwt.JwtService;
import com.petservice.main.user.service.Interface.RefreshTokenServiceInterface;
import com.petservice.main.user.service.Oauth2Service;
import com.petservice.main.user.service.Oauth2SuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

  @Value("${app.cors.enabled}")
  private boolean corsEnabled;

  private final JwtService jwtService;
  private final RefreshTokenServiceInterface refreshTokenService;

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring()
        .requestMatchers("/index.html","/assets/**", "/favicon.ico", "/*.png", "/*.svg", "/*.jpg", "/*.html",
            "/*.css", "/*.js","/asset-manifest.json","/static/**" );
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

    if(corsEnabled){
        http.cors(corsCustomizer->corsCustomizer.configurationSource(new CorsConfigurationSource() {
          @Override
          public CorsConfiguration getCorsConfiguration(@NonNull HttpServletRequest request) {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));
            configuration.setAllowedMethods(Collections.singletonList("*"));
            configuration.setAllowCredentials(true);
            configuration.setAllowedHeaders(Collections.singletonList("*"));
            configuration.setMaxAge(3600L);
            configuration.setExposedHeaders(Collections.singletonList("Authorization"));
            configuration.addExposedHeader("Set-Cookie");
            return configuration;
          }
        })
      );
    }

    http.csrf((auth) -> auth.disable());
    http.formLogin((auth) -> auth.disable());
    http.httpBasic((auth) -> auth.disable());
    http.addFilterBefore(new JwtFilter(jwtService,refreshTokenService), UsernamePasswordAuthenticationFilter.class);

    http.oauth2Login(oauth2 -> oauth2
        .loginPage("/user/login")
        .userInfoEndpoint((userInfo)->userInfo.userService(oauth2Service()))
      .successHandler(oauth2SuccessHandler())
      .authorizationEndpoint(authEndpoint->authEndpoint.baseUri("/oauth2/authorization"))
      .redirectionEndpoint(redirectEndpoint->redirectEndpoint.baseUri("/login/oauth2/code/**")));

    http.sessionManagement(
      (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.authorizeHttpRequests(auth -> auth
      .requestMatchers("/user/**","/api/user/**","/oauth2/**","/**")
        .permitAll()
        .requestMatchers("/api/**").authenticated()
        .anyRequest().permitAll()
    );

    http.exceptionHandling(exception ->
        exception
            .authenticationEntryPoint(customAuthenticationEntryPoint())
            .accessDeniedHandler((req, res, exc) -> { throw exc; })
    );

    return http.build();
  }

  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public WebServerFactoryCustomizer<TomcatServletWebServerFactory> containerCustomizer() {
    return factory -> {
      ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/index.html");
      factory.addErrorPages(error404Page);
    };
  }

  @Bean
  public Oauth2Service oauth2Service(){
    return new Oauth2Service();
  }

  @Bean
  public Oauth2SuccessHandler oauth2SuccessHandler(){
    return new Oauth2SuccessHandler();
  }

  @Bean
  public AuthenticationEntryPoint customAuthenticationEntryPoint() {
    return new CustomAuthenticationEntryPoint();
  }
}
