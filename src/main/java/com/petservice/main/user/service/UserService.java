package com.petservice.main.user.service;

import com.petservice.main.business.database.dto.PetBusinessDTO;
import com.petservice.main.business.database.entity.PetBusiness;
import com.petservice.main.business.database.mapper.PetBusinessMapper;
import com.petservice.main.business.database.repository.PetBusinessRepository;
import com.petservice.main.business.service.Interface.PetBusinessServiceInterface;
import com.petservice.main.user.database.dto.CustomUserDetails;
import com.petservice.main.user.database.dto.UserDTO;
import com.petservice.main.user.database.entity.Role;
import com.petservice.main.user.database.entity.User;
import com.petservice.main.user.database.entity.UserType;
import com.petservice.main.user.database.mapper.UserMapper;
import com.petservice.main.user.database.repository.UserRepository;
import com.petservice.main.user.service.Interface.CustomUserServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
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

  private final PetBusinessServiceInterface petBusinessServiceInterface;

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
  public UserDTO registerUser(UserDTO userDTO){
    User user=null;
    PetBusiness petBusiness=null;

    if(!UserValidation(userDTO)){
      throw new IllegalArgumentException("Data is not valid");
    }

    if(userRepository.findByUserLoginId(userDTO.getUserLoginId()).isPresent()){
      throw new IllegalArgumentException("LoginId already in use");
    }

    if(userRepository.existsByEmail(userDTO.getEmail()) ||
        userRepository.existsByPhone(userDTO.getPhone())){
      throw new IllegalArgumentException("Email or Phone already in use");
    }
    userDTO.setCreateAt(LocalDateTime.now());
    userDTO.setUpdateAt(LocalDateTime.now());

    PetBusinessDTO petBusinessDTO = userDTO.getPetBusinessDTO();

    try {
      if (userDTO.getRole() == Role.SERVICE_PROVIDER && petBusinessDTO != null) {

        if (petBusinessServiceInterface.existBusiness(petBusinessDTO)) {
          throw new IllegalArgumentException("Business Information Invalid!!");
        }

        //먼저 유저 정보를 저장하고 사업자 정보를 저장
        userDTO.setPetBusinessDTO(null);
        User newuser = userMapper.toEntity(userDTO);
        newuser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user = userRepository.save(newuser);

        petBusinessDTO.setUserId(user.getId());

        petBusiness = petBusinessMapper.toEntity(petBusinessDTO);
        petBusinessRepository.save(petBusiness);

        user.setPetBusiness(petBusiness);

      } else {
        User newuser = userMapper.toEntity(userDTO);
        newuser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user = userRepository.save(newuser);
      }
    }catch (Exception e){
      log.info(e.getMessage());
      throw new RuntimeException("회원 가입 중 문제 발생!!, 다시 해주세요.");
    }

    return userMapper.toBasicDTO(user);
  }

  //관리자 회원 가입
  @Override
  @Transactional
  public UserDTO registerMasterUser(UserDTO userDTO){

    User user=null;
    if(userDTO == null){
      throw new IllegalArgumentException("Data is not valid");
    }

    userDTO.setRole(Role.MANAGER);

    if(!UserValidation(userDTO)){
      throw new IllegalArgumentException("Data is not valid");
    }

    if(userRepository.findByUserLoginId(userDTO.getUserLoginId()).isPresent()){
      throw new IllegalArgumentException("LoginId already in use");
    }

    if(userRepository.existsByEmail(userDTO.getEmail()) ||
        userRepository.existsByPhone(userDTO.getPhone())){
      throw new IllegalArgumentException("Email or Phone already in use");
    }
    userDTO.setCreateAt(LocalDateTime.now());
    userDTO.setUpdateAt(LocalDateTime.now());

    User newuser = userMapper.toEntity(userDTO);
    newuser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    user = userRepository.save(newuser);
    return userMapper.toBasicDTO(user);
  }

  //로그인 기능
  @Override
  @Transactional(readOnly = true)
  public UserDTO UserLogin(String UserLoginId, String Password){

    Optional<User> userOptional=userRepository.findByUserLoginId(UserLoginId);

    if (!userOptional.isPresent()){
      throw new BadCredentialsException("Invalid ID or password");
    }

    User user = userOptional.get();

    if(user.getPassword()==null || !passwordEncoder.matches(Password,user.getPassword())){
      throw new BadCredentialsException("Invalid ID or password");
    }

    return userMapper.toBasicDTO(user);
  }

  //사용자 인증 객체로 회원 정보 조회
  @Override
  @Transactional(readOnly = true)
  public UserDTO getUserFromPrincipal(CustomUserDetails userDetails){
    if (userDetails==null){
      throw new UsernameNotFoundException("User is not authenticated");
    }
    User user = userRepository.findByUserLoginIdWithBusiness(userDetails.getUsername())
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return userMapper.toBasicDTO(user);
  }

  //회원 정보 조회 1
  @Override
  @Transactional(readOnly = true)
  public UserDTO getUserByEmail(String email){
    return userMapper.toBasicDTO(userRepository.findByEmail(email));
  }

  //회원 정보 조회 2
  public UserDTO getUserByLoginId(String LoginId){
    User findUser=userRepository.findByUserLoginId(LoginId).orElse(null);
    if(findUser !=null){
      return userMapper.toBasicDTO(findUser);
    }
    else{
      return null;
    }
  }

  //회원 기본 정보 수정(일부 제외)
  //추후 수정 가능
  @Override
  @Transactional
  public UserDTO UpdateUser(CustomUserDetails userDetails, UserDTO userDTO){

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
    if(existUser.getLoginType() != UserType.GOOGLE) {
      existUser.setEmail(userDTO.getEmail());
    }
    existUser.setPhone(userDTO.getPhone());
    if(existUser.getRole() ==Role.SERVICE_PROVIDER){
      PetBusinessDTO updatePetBusiness= userDTO.getPetBusinessDTO();
      PetBusiness existPetBusiness= existUser.getPetBusiness();

      existPetBusiness.setBusinessName(updatePetBusiness.getBusinessName());
      existPetBusiness.setBankAccount(updatePetBusiness.getBankAccount());
      existPetBusiness.setMinPrice(updatePetBusiness.getMinPrice());
      existPetBusiness.setMaxPrice(updatePetBusiness.getMaxPrice());
      existPetBusiness.setFacilities(updatePetBusiness.getFacilities());
      existPetBusiness.setDescription(updatePetBusiness.getDescription());
      existPetBusiness.setUpdatedAt(LocalDateTime.now());

      existUser.setPetBusiness(existPetBusiness);
    }
    existUser.setUpdatedAt(LocalDateTime.now());
    User user=userRepository.save(existUser);

    return userMapper.toBasicDTO(user);
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
      if(!passwordEncoder.matches(Password,delete.getPassword())) {

        return false;

      }else{
        //회원의 사업자 정보, 북마크, 펫 정보 삭제(자동)
        if(delete.getRole().equals(Role.SERVICE_PROVIDER)){
          PetBusiness deletePetBusiness=petBusinessRepository.findByUser_Id(delete.getId());
          petBusinessRepository.deleteById(deletePetBusiness.getId());
          userRepository.deleteByUserLoginId(delete.getUserLoginId());

        }else {
          userRepository.deleteByUserLoginId(delete.getUserLoginId());
        }

      }
    }else{
      log.error("User not found with LoginId: {}", userDetails.getUsername());
      return false;
    }
    return true;
  }

  @Override
  public boolean UserValidation(UserDTO userDTO){
    try {
      if (userDTO == null) {
        return false;
      }

      if (isBlank(userDTO.getUserLoginId())
          || isBlank(userDTO.getEmail())
          || isBlank(userDTO.getRole().name())
          || isBlank(userDTO.getName())
          || isBlank(userDTO.getPhone())) {
        return false;
      }

      if (userDTO.getRole().equals(Role.SERVICE_PROVIDER)) {
        PetBusinessDTO petBusinessDTO = userDTO.getPetBusinessDTO();

        if (petBusinessDTO == null) {
          return false;
        }

        if (isBlank(petBusinessDTO.getBusinessName())
          || isBlank(petBusinessDTO.getStatus().name())
          || isBlank(petBusinessDTO.getAvgRate().toString())
          || isBlank(petBusinessDTO.getRegistrationNumber())
          || isBlank(petBusinessDTO.getBankAccount())
          || isBlank(petBusinessDTO.getVarification().name())) {
          return false;
        }

        if(!petBusinessServiceInterface.BusinessValidation(petBusinessDTO)){
          return false;
        }

      }
    }catch (Exception e){
      log.error("회원 DTO 체크 중 에러 발생!!, {}",e.getMessage());
      return false;
    }
    return true;
  }

  @Override
  public boolean isBlank(String str){
    return str == null || str.trim().isEmpty();
  }
}
