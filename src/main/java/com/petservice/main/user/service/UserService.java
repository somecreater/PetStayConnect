package com.petservice.main.user.service;

import com.petservice.main.user.database.dto.CustomUserDetails;
import com.petservice.main.user.database.dto.UserDTO;
import com.petservice.main.user.database.entity.User;
import com.petservice.main.user.database.mapper.UserMapper;
import com.petservice.main.user.database.repository.UserRepository;
import com.petservice.main.user.service.Interface.CustomUserServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements CustomUserServiceInterface, UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  private final UserMapper userMapper;

  //아이디로 사용자 인증 객체 생성
  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> userOptional=userRepository.findByUserLoginId(username);
    if (userOptional.isEmpty()) {
      throw new UsernameNotFoundException("User not found with LoginID: " + username);
    }
    return new CustomUserDetails(userOptional.get());
  }

  //회원 가입 기능
  @Override
  @Transactional
  public User registerUser(UserDTO userDTO){
    if(userRepository.findByUserLoginId(userDTO.getUserLoginId()).isPresent()){
      throw new IllegalArgumentException("LoginId already in use");
    }
    if(userRepository.existsByEmail(userDTO.getEmail())){
      throw new IllegalArgumentException("Email already in use");
    }

    User newuser=userMapper.toEntity(userDTO);
    newuser.setPassword(passwordEncoder.encode(userDTO.getPassword()));

    return userRepository.save(newuser);
  }

  //로그인 기능
  @Override
  @Transactional(readOnly = true)
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

  //사용자 인증 객체로 회원 정보 조회
  @Override
  @Transactional(readOnly = true)
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

  //회원 정보 조회
  @Override
  @Transactional(readOnly = true)
  public User getUserByEmail(String email){
    return userRepository.findByEmail(email);
  }

  //회원 정보 수정(회원 아이디, 권한 제외)
  //추후 수정 가능
  @Override
  @Transactional
  public User UpdateUser(CustomUserDetails userDetails, UserDTO userDTO){

    if (userDetails == null || !UserValidation(userDTO)) {
      return null;
    }
    
    User existUser=userRepository.findByUserLoginId(userDTO.getUserLoginId()).orElse(null);
    if (existUser == null) {
      return null;
    }

    String roleName = userDetails.getAuthorities()
        .iterator()
        .next()
        .getAuthority();

    boolean sameUser = userDetails.getUsername().equals(userDTO.getUserLoginId());
    boolean sameRole = roleName.equals(userDTO.getRole().name());

    if(!sameUser || !sameRole){
      return null;
    }

    existUser.setName(userDTO.getName());
    existUser.setEmail(userDTO.getEmail());
    User user=userRepository.save(existUser);

    return user;
  }

  //회원 탈퇴 기능
  @Override
  @Transactional
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
  public boolean UserValidation(UserDTO userDTO){
    if(userDTO==null){
      return false;
    }
    return userDTO.getUserLoginId() != null && userDTO.getEmail() != null
      && userDTO.getRole() != null && userDTO.getName() != null;
  }
}
