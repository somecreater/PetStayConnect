package com.petservice.main.user.database.dto;

import com.petservice.main.user.database.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

  private final String username;
  private final String password;
  private final String name;
  private final Collection<? extends GrantedAuthority> authorities;

  public CustomUserDetails(String LoginId, String name, String role){
    this.username=LoginId;
    this.password=null;
    this.name=name;
    this.authorities = List.of(new SimpleGrantedAuthority(role));
  }
  public CustomUserDetails(User user) {
    this.username = user.getUserLoginId();
    this.password = user.getName();
    this.name = user.getName();
    this.authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
