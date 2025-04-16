package com.petservice.main.user.service;

import com.petservice.main.common.database.dto.CustomUserDetails;
import com.petservice.main.common.database.dto.UserDTO;
import com.petservice.main.common.database.entity.User;
import com.petservice.main.common.database.mapper.UserMapper;
import com.petservice.main.common.database.repository.UserRepository;
import com.petservice.main.user.service.Interface.CustomUserServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements CustomUserServiceInterface, UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  private final UserMapper userMapper;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> userOptional=userRepository.findByUserLoginId(username);
    if (userOptional.isEmpty()) {
      throw new UsernameNotFoundException("User not found with LoginID: " + username);
    }
    return new CustomUserDetails(userOptional.get());
  }

  @Override
  public User registerUser(UserDTO userDTO){
    if(userRepository.findByUserLoginId(userDTO.getUserLoginId()).isPresent()){
      throw new IllegalArgumentException("LoginId already in use");
    }

    User newuser=userMapper.toEntity(userDTO);
    newuser.setPassword(passwordEncoder.encode(userDTO.getPassword()));

    return userRepository.save(newuser);
  }

  @Override
  public User UserLogin(String UserLoginId, String Password){

    Optional<User> userOptional=userRepository.findByUserLoginId(UserLoginId);

    if (!userOptional.isPresent()){
      throw new BadCredentialsException("Invalid ID or password");
    }

    User user = userOptional.get();

    if(user.getPassword()==null || !passwordEncoder.matches(Password,user.getPassword())){
      throw new BadCredentialsException("Invalid ID or password");
    }

    return user;
  }

  @Override
  public User getUserFromPrincipal(CustomUserDetails userDetails){
    if (userDetails==null){
      throw new UsernameNotFoundException("User is not authenticated");
    }

    Optional<User> userOptional=userRepository.findByUserLoginId(userDetails.getUsername());

    if(!userOptional.isPresent()){
      log.error("User not found with LoginId: {}", userDetails.getUsername());
    }

    return userOptional.get();
  }

  @Override
  public User getUserByEmail(String email){
    return userRepository.findByEmail(email);
  }

  @Override
  public boolean DeleteUser(CustomUserDetails userDetails, String UserLoginId, String Password){
    if (userDetails==null){
      throw new UsernameNotFoundException("User is not authenticated");
    }

    Optional<User> userOptional=userRepository.findByUserLoginId(userDetails.getUsername());

    if(userOptional.isPresent()){
      User delete=userOptional.get();
      if(passwordEncoder.matches(Password,delete.getPassword())) {
        userRepository.deleteByUserLoginId(delete.getUserLoginId());
      }else{
        return false;
      }
    }else{
      log.error("User not found with LoginId: {}", userDetails.getUsername());
      return false;
    }
    return true;
  }
}
