package com.petservice.main.user.service;

import com.petservice.main.common.database.dto.CustomOAuth2UserDetail;
import com.petservice.main.common.database.dto.UserDTO;
import com.petservice.main.common.database.entity.Role;
import com.petservice.main.common.database.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class Oauth2Service extends DefaultOAuth2UserService {

  @Autowired
  private UserService userService;
  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    Map<String,Object> oAuthAttributes=oAuth2User.getAttributes();

    String email = oAuth2User.getAttribute("email");
    String name = oAuth2User.getAttribute("name");

    User user= userService.getUserByEmail(email);
    String userNameAttributeName=userRequest.getClientRegistration().getProviderDetails()
        .getUserInfoEndpoint().getUserNameAttributeName();

    if(user==null){
      UserDTO newUserDTO=Oauth2ToDTO(oAuth2User);
      user=userService.registerUser(newUserDTO);
    }

    return new CustomOAuth2UserDetail(oAuth2User.getAttributes(),userNameAttributeName,user);
  }

  public UserDTO Oauth2ToDTO(OAuth2User oAuth2User){
    UserDTO userDTO=new UserDTO();
    userDTO.setUserLoginId(oAuth2User.getAttribute("email"));
    userDTO.setPassword(bCryptPasswordEncoder.encode(UUID.randomUUID().toString()));
    userDTO.setRole(Role.CUSTOMER);
    userDTO.setEmail(oAuth2User.getAttribute("email"));
    userDTO.setName(oAuth2User.getAttribute("name"));

    return userDTO;
  }
}
