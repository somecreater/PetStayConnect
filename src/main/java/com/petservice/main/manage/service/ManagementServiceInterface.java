package com.petservice.main.manage.service;

import com.petservice.main.business.database.dto.ReservationDTO;
import com.petservice.main.payment.database.dto.PaymentDTO;
import com.petservice.main.qna.database.dto.QnaAnswerDTO;
import com.petservice.main.qna.database.dto.QnaPostDTO;
import com.petservice.main.review.database.dto.ReviewDTO;
import com.petservice.main.user.database.dto.UserDTO;
import org.springframework.data.domain.Page;

//관리자 기능
public interface ManagementServiceInterface {

  //회원 목록 한번에 가져와서 관리
  public Page<UserDTO> getUserByPage(int page, int size);
  public boolean forceDeleteUser(String userLoginId);
  public boolean sendAlertMail(String userLoginId, String subject, String content);

  //특정 기간 동안 올라온 QnA 게시글이나 리뷰를 가져와서 관리
  public Page<QnaAnswerDTO> getQnaAnswerByPage(int page, int size);
  public boolean deleteQna(Long id);
  public boolean deleteQnaAnswer(Long id);
  public Page<ReviewDTO> getReviewPage(int page, int size);
  public boolean deleteReview(Long id);

  //전체적인 예약이나 결제 관리
  public Page<ReservationDTO> getReservation(int page, int size);
  public Page<PaymentDTO> getPayment(int page, int size);

}
