package com.petservice.main.user.service;

import com.petservice.main.business.database.dto.PetBusinessDTO;
import com.petservice.main.business.database.entity.PetBusiness;
import com.petservice.main.business.database.mapper.PetBusinessMapper;
import com.petservice.main.business.database.mapper.PetBusinessTypeMapper;
import com.petservice.main.business.database.repository.PetBusinessRepository;
import com.petservice.main.business.service.Interface.BusinessServiceInterface;
import com.petservice.main.user.database.dto.CustomUserDetails;
import com.petservice.main.user.database.dto.UserDTO;
import com.petservice.main.user.database.entity.Role;
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

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements CustomUserServiceInterface, UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final PetBusinessRepository petBusinessRepository;

  private final BusinessServiceInterface businessServiceInterface;

  private final UserMapper userMapper;
  private final PetBusinessMapper petBusinessMapper;

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

  //회원 가입 기능(추후 사업 타입관련 기능 구현시 서비스로 수정)
  @Override
  @Transactional
  public User registerUser(UserDTO userDTO){
    User user=null;
    PetBusiness petBusiness=null;
    if(userRepository.findByUserLoginId(userDTO.getUserLoginId()).isPresent()){
      throw new IllegalArgumentException("LoginId already in use");
    }

    if(userRepository.existsByEmail(userDTO.getEmail()) ||
        userRepository.existsByPhone(userDTO.getPhone())){
      throw new IllegalArgumentException("Email or Phone already in use");
    }
    userDTO.setCreateAt(LocalDateTime.now());
    userDTO.setUpdateAt(LocalDateTime.now());

    if(userDTO.getRole().name().compareTo("SERVICE_PROVIDER") == 0 && userDTO.getPetBusinessDTO()!=null){

      if( businessServiceInterface.existBusiness(userDTO.getPetBusinessDTO()) ||
          !businessServiceInterface.BusinessValidation(userDTO.getPetBusinessDTO())){
        throw new IllegalArgumentException("Business Information Invalid!!");
      }

      //먼저 유저 정보를 저장하고 사업자 정보를 저장
      PetBusinessDTO petBusinessDTO=userDTO.getPetBusinessDTO();
      userDTO.setPetBusinessDTO(null);
      User newuser=userMapper.toEntity(userDTO);
      newuser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
      user=userRepository.save(newuser);

      petBusinessDTO.setUserId(user.getId());
      //나중에 수정
      petBusinessDTO.setPetBusinessTypeId(0L);
      petBusiness=petBusinessMapper.toEntity(petBusinessDTO);
      petBusinessRepository.save(petBusiness);

      user.setPetBusiness(petBusiness);

    }else{
      User newuser=userMapper.toEntity(userDTO);
      newuser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
      user=userRepository.save(newuser);
    }

    return user;
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

  //사용자 인증 객체로 회원 정보 조회(추후 수정)
  @Override
  @Transactional(readOnly = true)
  public User getUserFromPrincipal(CustomUserDetails userDetails){
    if (userDetails==null){
      throw new UsernameNotFoundException("User is not authenticated");
    }

    Optional<User> userOptional=userRepository.findByUserLoginId(userDetails.getUsername());

    if(!userOptional.isPresent()){
      log.error("User not found with LoginId: {}", userDetails.getUsername());
      return null;
    }

    User userInformation=userOptional.get();
    if(userInformation.getRole()==Role.SERVICE_PROVIDER){
      PetBusiness petBusiness= petBusinessRepository.findByUser_Id(userInformation.getId());
      petBusiness.setUser(userInformation);
      userInformation.setPetBusiness(petBusiness);
    }

    return userInformation;
  }

  //회원 정보 조회
  @Override
  @Transactional(readOnly = true)
  public User getUserByEmail(String email){
    return userRepository.findByEmail(email);
  }

  //회원 기본 정보 수정(일부 제외)
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
    existUser.setPhone(userDTO.getPhone());
    existUser.setUpdatedAt(LocalDateTime.now());
    User user=userRepository.save(existUser);

    return user;
  }

  //회원 탈퇴 기능(추후 수정)
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
        //회원의 사업자 정보, 북마크, 펫 정보 삭제(자동)
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
      && userDTO.getRole() != null && userDTO.getName() != null && userDTO.getPhone() != null;
  }
}
