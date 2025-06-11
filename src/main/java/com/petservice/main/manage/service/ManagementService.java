package com.petservice.main.manage.service;

import com.petservice.main.business.database.dto.ReservationDTO;
import com.petservice.main.business.database.entity.PetBusiness;
import com.petservice.main.business.database.entity.Reservation;
import com.petservice.main.business.database.mapper.ReservationMapper;
import com.petservice.main.business.database.repository.ReservationRepository;
import com.petservice.main.business.service.Interface.*;
import com.petservice.main.common.service.MailServiceInterface;
import com.petservice.main.payment.database.dto.PaymentDTO;
import com.petservice.main.payment.database.entity.Account;
import com.petservice.main.payment.database.entity.Payment;
import com.petservice.main.payment.database.mapper.PaymentMapper;
import com.petservice.main.payment.database.repository.AccountRepository;
import com.petservice.main.payment.database.repository.PaymentRepository;
import com.petservice.main.qna.database.dto.QnaAnswerDTO;
import com.petservice.main.qna.database.dto.QnaPostDTO;
import com.petservice.main.qna.database.entity.QnaAnswer;
import com.petservice.main.qna.database.entity.QnaPost;
import com.petservice.main.qna.database.mapper.QnaAnswerMapper;
import com.petservice.main.qna.database.mapper.QnaPostMapper;
import com.petservice.main.qna.database.repository.QnaAnswerRepository;
import com.petservice.main.qna.database.repository.QnaPostRepository;
import com.petservice.main.review.database.dto.ReviewDTO;
import com.petservice.main.review.database.entity.Review;
import com.petservice.main.review.database.mapper.ReviewMapper;
import com.petservice.main.review.database.repository.ReviewRepository;
import com.petservice.main.user.database.dto.UserDTO;
import com.petservice.main.user.database.entity.Role;
import com.petservice.main.user.database.entity.User;
import com.petservice.main.user.database.mapper.UserMapper;
import com.petservice.main.user.database.repository.PetRepository;
import com.petservice.main.user.database.repository.UserRepository;
import com.petservice.main.user.service.Interface.CustomUserServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementService implements ManagementServiceInterface{

  private final PetBusinessRoomServiceInterface petBusinessRoomServiceInterface;
  private final PetReservationServiceInterface petReservationServiceInterface;
  private final ReservationServiceInterface reservationServiceInterface;
  private final PetBusinessServiceInterface petBusinessServiceInterface;
  private final PetBusinessTagServiceInterface petBusinessTagServiceInterface;
  private final MailServiceInterface mailServiceInterface;

  private final PaymentRepository paymentRepository;
  private final AccountRepository accountRepository;
  private final PetRepository petRepository;
  private final QnaAnswerRepository qnaAnswerRepository;
  private final QnaPostRepository qnaPostRepository;
  private final ReviewRepository reviewRepository;
  private final ReservationRepository reservationRepository;
  private final UserRepository userRepository;

  private final PaymentMapper paymentMapper;
  private final ReviewMapper reviewMapper;
  private final QnaAnswerMapper qnaAnswerMapper;
  private final ReservationMapper reservationMapper;
  private final UserMapper userMapper;

  @Override
  @Transactional(readOnly = true)
  public Page<UserDTO> getUserByPage(int page, int size) {
    Pageable pageable= PageRequest.of(page,size, Sort.by("updatedAt").descending());
    Page<User> users=userRepository.findAll(pageable);
    return users.map(userMapper::toBasicDTO);
  }

  @Override
  public boolean forceDeleteUser(String userLoginId) {
    try {

      Optional<User> userOptional = userRepository.findByUserLoginId(userLoginId);

      if (userOptional.isPresent()) {

        User delete = userOptional.get();
        Account account = accountRepository.findByUser_Id(delete.getId());
        if(delete.getRole().equals(Role.SERVICE_PROVIDER)){
          PetBusiness deleteBusiness= delete.getPetBusiness();
          delete.setPetBusiness(null);
          if(account !=null) {
            accountRepository.delete(account);
          }
          if (!petReservationServiceInterface.deletePetReservationByBusinessId(
              deleteBusiness.getId())
              || !petReservationServiceInterface.deletePetReservationByUserId(delete.getId())
              || !reservationServiceInterface.updateDeleteBusiness(deleteBusiness.getId())
              || !reservationServiceInterface.updateDeleteUser(delete.getId())
              || !petBusinessRoomServiceInterface.deleteRoomByBusiness(deleteBusiness.getId())
              || !petBusinessTagServiceInterface.deleteTagByBusinessId(deleteBusiness.getId())
              || !petBusinessServiceInterface.deleteBusinessByUser(delete.getId())
              || !petBusinessServiceInterface.deleteBusiness(deleteBusiness.getId())
              || petRepository.deleteByUser_Id(delete.getId()) <0) {
            throw new RuntimeException("회원 강제 탈퇴가 비정상적으로 실행되었습니다.");
          }
          userRepository.delete(delete);

        }else{
          if(account != null) {
            accountRepository.delete(account);
          }
          if(!petReservationServiceInterface.deletePetReservationByUserId(delete.getId())
              || !reservationServiceInterface.updateDeleteUser(delete.getId())
              || petRepository.deleteByUser_Id(delete.getId()) <0) {
            throw new RuntimeException("회원 강제 탈퇴가 비정상적으로 실행되었습니다." );
          }
          userRepository.delete(delete);
        }

      }else{
        log.error("User not found with LoginId: {}", userLoginId);
        return false;
      }
      return true;
    } catch (Exception e) {
      log.info("회원 강제 탈퇴 중 오류 발생! {}", e.getMessage());
      e.printStackTrace();
      return false;
    }
  }

  @Override
  @Transactional(readOnly = true)
  public boolean sendAlertMail(String userLoginId, String subject, String content) {
    try {
      User user = userRepository.findByUserLoginId(userLoginId).orElse(null);
      if (user == null) {
        return false;
      }
      String toMail = user.getEmail();
      mailServiceInterface.sendMail(toMail, subject, content);
      return true;
    }catch (Exception e){
      e.printStackTrace();
      log.info("메일 보내기에 실패하였습니다.");
      return false;
    }
  }

  @Override
  public Page<QnaAnswerDTO> getQnaAnswerByPage(int page, int size) {
    Pageable pageable= PageRequest.of(page,size, Sort.by("updatedAt").descending());
    Page<QnaAnswer> qnaAnswerPage= qnaAnswerRepository.findAll(pageable);
    return qnaAnswerPage.map(qnaAnswerMapper::toDTO);
  }

  @Override
  public boolean deleteQna(Long id) {
    try {
      qnaAnswerRepository.deleteByPost_Id(id);
      qnaPostRepository.deleteById(id);
      return true;
    }catch (Exception e){
      e.printStackTrace();
      log.info("질문 강제 삭제중 오류 발생");
      return false;
    }
  }

  @Override
  public boolean deleteQnaAnswer(Long id) {
    try{
      qnaAnswerRepository.deleteById(id);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      log.info("답변 강제 삭제중 오류 발생");
      return false;
    }
  }

  @Override
  public Page<ReviewDTO> getReviewPage(int page, int size) {
    Pageable pageable=PageRequest.of(page,size,Sort.by("updatedAt").descending());
    Page<Review> reviews=reviewRepository.findAll(pageable);
    return reviews.map(reviewMapper::toDTO);
  }

  @Override
  public boolean deleteReview(Long id) {
    try {
      reviewRepository.deleteById(id);
      return true;
    }catch (Exception e){
      e.printStackTrace();
      log.info("리뷰 강제 삭제중 오류 발생");
      return false;
    }
  }

  @Override
  public Page<ReservationDTO> getReservation(int page, int size) {
    Pageable pageable=PageRequest.of(page,size,Sort.by("updatedAt").descending());
    Page<Reservation> reservations= reservationRepository.findAll(pageable);
    return reservations.map(reservationMapper::toBasicDTO);
  }

  @Override
  public Page<PaymentDTO> getPayment(int page, int size) {
    Pageable pageable=PageRequest.of(page,size,Sort.by("updatedAt").descending());
    Page<Payment> payments=paymentRepository.findAll(pageable);
    return payments.map(paymentMapper::toDTO);
  }
}
