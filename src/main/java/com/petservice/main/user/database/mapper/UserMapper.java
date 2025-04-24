package com.petservice.main.user.database.mapper;

import com.petservice.main.business.database.mapper.PetBusinessMapper;
import com.petservice.main.business.database.mapper.ReservationMapper;
import com.petservice.main.qna.database.mapper.QnaAnswerMapper;
import com.petservice.main.qna.database.mapper.QnaPostMapper;
import com.petservice.main.review.database.mapper.ReviewMapper;
import com.petservice.main.user.database.dto.UserDTO;
import com.petservice.main.user.database.entity.User;
import com.petservice.main.user.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

  private final UserRepository userRepository;

  private final PetMapper petMapper;
  private final PetBusinessMapper petBusinessMapper;
  private final BookmarkMapper bookmarkMapper;
  private final QnaPostMapper qnaPostMapper;
  private final QnaAnswerMapper qnaAnswerMapper;
  private final ReservationMapper reservationMapper;
  private final ReviewMapper reviewMapper;

  public User toEntity(UserDTO userDTO){
    User user=new User();
    user.setId(userDTO.getId());
    user.setUserLoginId(userDTO.getUserLoginId());
    user.setEmail(userDTO.getEmail());
    user.setRole(userDTO.getRole());
    user.setName(userDTO.getName());
    user.setPhone(userDTO.getPhone());
    user.setLoginType(userDTO.getLoginType());
    user.setPetNumber(userDTO.getPetNumber());
    user.setQnaScore(userDTO.getQnaScore());
    user.setPoint(userDTO.getPoint());
    user.setCreatedAt(userDTO.getCreateAt());
    user.setUpdatedAt(userDTO.getUpdateAt());
    if(userDTO.getPetBusinessDTO()!=null) {
      user.setPetBusiness(petBusinessMapper.toEntity(userDTO.getPetBusinessDTO()));
    }
    if(userDTO.getPetDTOList() != null){
      user.setPetList(userDTO.getPetDTOList().stream()
          .map(petMapper::toEntity).toList());
    }
    if(userDTO.getBookmarkDTOList() != null){
      user.setBookmarkList(userDTO.getBookmarkDTOList().stream()
          .map(bookmarkMapper::toEntity).toList());
    }
    if(userDTO.getQnaPostDTOList() != null){
      user.setQnaPostList(userDTO.getQnaPostDTOList().stream()
          .map(qnaPostMapper::toEntity).toList());
    }
    if(userDTO.getQnaAnswerDTOList() != null){
      user.setQnaAnswerList(userDTO.getQnaAnswerDTOList().stream()
          .map(qnaAnswerMapper::toEntity).toList());
    }
    if(userDTO.getReservationDTOList() != null){
      user.setReservationList(userDTO.getReservationDTOList().stream()
          .map(reservationMapper::toEntity).toList());
    }
    if(userDTO.getReviewDTOList() != null){
      user.setReviewList(userDTO.getReviewDTOList().stream()
          .map(reviewMapper::toEntity).toList());
    }
    return user;
  }

  public UserDTO toDTO(User user){
    UserDTO userdto=new UserDTO();
    userdto.setId(user.getId());
    userdto.setUserLoginId(user.getUserLoginId());
    userdto.setName(user.getName());
    userdto.setEmail(user.getEmail());
    userdto.setRole(user.getRole());
    userdto.setPhone(user.getPhone());
    userdto.setLoginType(user.getLoginType());
    userdto.setPetNumber(user.getPetNumber());
    userdto.setQnaScore(user.getQnaScore());
    userdto.setPoint(user.getPoint());
    userdto.setCreateAt(user.getCreatedAt());
    userdto.setUpdateAt(user.getUpdatedAt());
    if(user.getPetBusiness()!=null) {
      userdto.setPetBusinessDTO(petBusinessMapper.toDTO(user.getPetBusiness()));
    }
    if(user.getPetList() != null){
      userdto.setPetDTOList(user.getPetList().stream()
          .map(petMapper::toDTO).toList());
    }
    if(user.getBookmarkList() != null){
      userdto.setBookmarkDTOList(user.getBookmarkList().stream()
          .map(bookmarkMapper::toDTO).toList());
    }
    if(user.getQnaPostList() != null){
      userdto.setQnaPostDTOList(user.getQnaPostList().stream()
          .map(qnaPostMapper::toDTO).toList());
    }
    if(user.getQnaAnswerList() != null){
      userdto.setQnaAnswerDTOList(user.getQnaAnswerList().stream()
          .map(qnaAnswerMapper::toDTO).toList());
    }
    if(user.getReservationList() != null){
      userdto.setReservationDTOList(user.getReservationList().stream()
          .map(reservationMapper::toDTO).toList());
    }
    if(user.getReviewList() != null){
      userdto.setReviewDTOList(user.getReviewList().stream()
          .map(reviewMapper::toDTO).toList());
    }
    return userdto;
  }
  //리스트 정보는 제외
  public UserDTO toBasicDTO(User user){
    UserDTO userdto=new UserDTO();
    userdto.setId(user.getId());
    userdto.setUserLoginId(user.getUserLoginId());
    userdto.setName(user.getName());
    userdto.setEmail(user.getEmail());
    userdto.setRole(user.getRole());
    userdto.setPhone(user.getPhone());
    userdto.setLoginType(user.getLoginType());
    userdto.setPetNumber(user.getPetNumber());
    userdto.setQnaScore(user.getQnaScore());
    userdto.setPoint(user.getPoint());
    userdto.setCreateAt(user.getCreatedAt());
    userdto.setUpdateAt(user.getUpdatedAt());
    if(user.getPetBusiness()!=null) {
      userdto.setPetBusinessDTO(petBusinessMapper.toBasicDTO(user.getPetBusiness()));
    }
    return userdto;
  }
}
