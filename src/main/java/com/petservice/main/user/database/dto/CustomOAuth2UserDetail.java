package com.petservice.main.user.database.dto;

import com.petservice.main.user.database.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;


public class CustomOAuth2UserDetail implements OAuth2User {

  private Map<String, Object> attributes;
  private String nameAttributeKey;
  private User user;

  public CustomOAuth2UserDetail( Map<String, Object> attributes, String nameAttributeKey,
      User user) {
    this.attributes = attributes;
    this.nameAttributeKey = nameAttributeKey;
    this.user = user;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(()->user.getRole().name());
  }

  @Override
  public String getName() {
    return user.getEmail();
  }

  public String getUsername(){
    return user.getName();
  }
}
