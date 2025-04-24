package com.petservice.main.user.database.dto;

import com.petservice.main.business.database.dto.PetBusinessDTO;
import com.petservice.main.business.database.dto.ReservationDTO;
import com.petservice.main.qna.database.dto.QnaAnswerDTO;
import com.petservice.main.qna.database.dto.QnaPostDTO;
import com.petservice.main.review.database.dto.ReviewDTO;
import com.petservice.main.user.database.entity.Role;
import com.petservice.main.user.database.entity.UserType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO {

  private Long id;
  private String userLoginId;
  private String password;
  private String email;
  private String name;
  private Role role;
  private String phone;
  private UserType loginType;
  private Integer petNumber;
  private Integer qnaScore;
  private Integer point;
  private LocalDateTime createAt;
  private LocalDateTime updateAt;
  private PetBusinessDTO petBusinessDTO;
  private List<PetDTO> petDTOList = new ArrayList<>();
  private List<BookmarkDTO> bookmarkDTOList = new ArrayList<>();
  private List<QnaPostDTO> qnaPostDTOList = new ArrayList<>();
  private List<QnaAnswerDTO> qnaAnswerDTOList = new ArrayList<>();
  private List<ReservationDTO> reservationDTOList =new ArrayList<>();
  private List<ReviewDTO> reviewDTOList = new ArrayList<>();
}
