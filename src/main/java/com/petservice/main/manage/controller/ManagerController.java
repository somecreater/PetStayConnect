package com.petservice.main.manage.controller;


import com.petservice.main.business.database.dto.ReservationDTO;
import com.petservice.main.manage.database.dto.AnnouncementDTO;
import com.petservice.main.manage.database.entity.SendMail;
import com.petservice.main.manage.service.AnnouncementServiceInterface;
import com.petservice.main.manage.service.ManagementServiceInterface;
import com.petservice.main.payment.database.dto.PaymentDTO;
import com.petservice.main.qna.database.dto.QnaAnswerDTO;
import com.petservice.main.qna.database.dto.QnaPostDTO;
import com.petservice.main.qna.service.post.QnaPostServiceInterface;
import com.petservice.main.review.database.dto.ReviewDTO;
import com.petservice.main.user.database.dto.CustomUserDetails;
import com.petservice.main.user.database.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/manage")
@RequiredArgsConstructor
@Slf4j
public class ManagerController {

  private final AnnouncementServiceInterface announcementService;
  private final ManagementServiceInterface managementService;
  private final QnaPostServiceInterface qnaPostService;

  @GetMapping("/announce")
  public ResponseEntity<?> getAnnouncementList(
      @AuthenticationPrincipal CustomUserDetails principal,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size
  ){
    Map<String, Object> result = new HashMap<>();
    Page<AnnouncementDTO> announcementDTOS= announcementService.announcementList(page,size);

    if(announcementDTOS != null && announcementDTOS.getTotalElements() > 0){
      result.put("result", true);
      result.put("message", "공지 목록입니다.");
      result.put("announcementList", announcementDTOS.getContent());
      result.put("totalPages", announcementDTOS.getTotalPages());
      result.put("currentPage", announcementDTOS.getNumber());
    }else{
      result.put("result", false);
      result.put("message","공지사항이 존재하지 않습니다.");
    }
    return ResponseEntity.ok(result);
  }

  @GetMapping("/announce/priority")
  public ResponseEntity<?> getPriorityAnnouncement(
      @AuthenticationPrincipal CustomUserDetails principal){
    Map<String, Object> result = new HashMap<>();
    List<AnnouncementDTO> priority= announcementService.PriorityAnnouncementList();

    if(priority != null){
      result.put("result", true);
      result.put("message", "공지 목록입니다.");
      result.put("announcementList", priority);
    }else{
      result.put("result", false);
      result.put("message","공지사항이 존재하지 않습니다.");
    }
    return ResponseEntity.ok(result);
  }

  @GetMapping("/announce/{announce_id}")
  public ResponseEntity<?> getAnnouncement(
      @AuthenticationPrincipal CustomUserDetails principal,
      @PathVariable("announce_id") Long announce_id){
    Map<String, Object> result = new HashMap<>();
    AnnouncementDTO announcementDTO = announcementService.getAnnouncement(announce_id);

    if(announcementDTO != null){
      result.put("result", true);
      result.put("message", "공지사항 입니다.");
      result.put("announcement", announcementDTO);
    }else{
      result.put("result", false);
      result.put("message", "공지사항이 존재하지 않습니다.");
    }
    return ResponseEntity.ok(result);
  }

  @PreAuthorize("hasAuthority('MANAGER')")
  @PostMapping("/announce")
  public ResponseEntity<?> registerAnnouncement(
      @AuthenticationPrincipal CustomUserDetails principal,
      @RequestBody AnnouncementDTO announcementDTO){
    Map<String, Object> result = new HashMap<>();

    AnnouncementDTO register= announcementService.registerAnnouncement(announcementDTO);
    if(register != null){
      result.put("result", true);
      result.put("message", "공지사항을 등록하였습니다.");
      result.put("announcement", register);
    }else{
      result.put("result", false);
      result.put("message", "공지사항 등록을 실패하였습니다.");
    }
    return ResponseEntity.ok(result);
  }

  @PreAuthorize("hasAuthority('MANAGER')")
  @PutMapping("/announce/{announce_id}")
  public ResponseEntity<?> updateAnnouncement(
      @AuthenticationPrincipal CustomUserDetails principal,
      @PathVariable("announce_id") Long announce_id,
      @RequestBody AnnouncementDTO announcementDTO){
    Map<String, Object> result = new HashMap<>();

    AnnouncementDTO update= announcementService.updateAnnouncement(announcementDTO);
    if(update != null){
      result.put("result", true);
      result.put("message", "공지사항을 수정하였습니다");
      result.put("announcement", update);
    }else{
      result.put("result", false);
      result.put("message", "공지사항 수정을 실패하였습니다.");
    }
    return ResponseEntity.ok(result);
  }

  @PreAuthorize("hasAuthority('MANAGER')")
  @DeleteMapping("/announce/{announce_id}")
  public ResponseEntity<?> deleteAnnouncement(
      @AuthenticationPrincipal CustomUserDetails principal,
      @PathVariable("announce_id") Long announce_id){
    Map<String, Object> result = new HashMap<>();

    if(announcementService.deleteAnnouncement(announce_id)){
      result.put("result", true);
      result.put("message", "공지사항을 삭제하였습니다");
    }else{
      result.put("result", false);
      result.put("message", "공지사항 삭제에 실패하였습니다");
    }
    return ResponseEntity.ok(result);
  }

  @PreAuthorize("hasAuthority('MANAGER')")
  @GetMapping("/user")
  public ResponseEntity<?> getUser(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size){
    Map<String, Object> result = new HashMap<>();
    Page<UserDTO> userDTOS=managementService.getUserByPage(page, size);
    if(userDTOS != null && userDTOS.getTotalElements()>0){
      result.put("result", true);
      result.put("message", "유저 목록입니다.");
      result.put("userList", userDTOS.getContent());
      result.put("totalPages", userDTOS.getTotalPages());
      result.put("currentPage", userDTOS.getNumber());
    }else {
      result.put("result", false);
      result.put("message", "유저 목록이 존재하지 않습니다.");
    }
    return ResponseEntity.ok(result);
  }
  @PreAuthorize("hasAuthority('MANAGER')")
  @DeleteMapping("/user/{userLoginId}")
  public ResponseEntity<?> deleteForceUser(
      @PathVariable("userLoginId") String userLoginId
  ){
    Map<String, Object> result = new HashMap<>();
    if(managementService.forceDeleteUser(userLoginId)){
      result.put("result", true);
      result.put("message", userLoginId+" 회원이 강제 삭제되었습니다.");
    }else{
      result.put("result", false);
      result.put("message", userLoginId+" 회원이 강제 삭제에 실패했습니다.");
    }
    return ResponseEntity.ok(result);
  }
  @PreAuthorize("hasAuthority('MANAGER')")
  @PostMapping("/user/send")
  public ResponseEntity<?> sendAlert(
      @RequestBody SendMail sendMail){
    Map<String, Object> result = new HashMap<>();
    if(managementService.sendAlertMail(
        sendMail.getUserLoginId(),sendMail.getTitle(),sendMail.getContent())){
      result.put("result", true);
      result.put("message", sendMail.getUserLoginId()+" 회원에 메일을 보냈습니다.");
    }else{
      result.put("result", false);
      result.put("message", sendMail.getUserLoginId()+" 회원에 메일을 보내기에 실패했습니다.");
    }
    return ResponseEntity.ok(result);
  }

  @PreAuthorize("hasAuthority('MANAGER')")
  @GetMapping("/qna/post")
  public ResponseEntity<?> getQnaPost(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size){
    Map<String, Object> result = new HashMap<>();
    Page<QnaPostDTO> qnaPostDTOS= qnaPostService.getPostsPage(page, size);
    if(qnaPostDTOS != null && qnaPostDTOS.getTotalElements()>0){
      result.put("result", true);
      result.put("message", "질문 목록입니다.");
      result.put("postList", qnaPostDTOS.getContent());
      result.put("totalPages", qnaPostDTOS.getTotalPages());
      result.put("currentPage", qnaPostDTOS.getNumber());
    }else{
      result.put("result", false);
      result.put("message", "질문 목록 없습니다.");
    }
    return ResponseEntity.ok(result);
  }
  @PreAuthorize("hasAuthority('MANAGER')")
  @GetMapping("/qna/answer")
  public ResponseEntity<?> getQnaAnswer(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size){
    Map<String, Object> result = new HashMap<>();
    Page<QnaAnswerDTO> qnaAnswerDTOS= managementService.getQnaAnswerByPage(page, size);
    if(qnaAnswerDTOS != null && qnaAnswerDTOS.getTotalElements()>0){
      result.put("result", true);
      result.put("message", "답변 목록입니다.");
      result.put("answerList", qnaAnswerDTOS.getContent());
      result.put("totalPages", qnaAnswerDTOS.getTotalPages());
      result.put("currentPage", qnaAnswerDTOS.getNumber());
    }else{
      result.put("result", false);
      result.put("message", "답변 목록 없습니다.");
    }
    return ResponseEntity.ok(result);
  }
  @PreAuthorize("hasAuthority('MANAGER')")
  @DeleteMapping("/qna/post/{id}")
  public ResponseEntity<?> deleteForcePost(
      @PathVariable("id") Long id){
    Map<String, Object> result = new HashMap<>();
    if(managementService.deleteQna(id)){
      result.put("result",true);
      result.put("message",id + " 질문이 삭제되었습니다");
    }else{
      result.put("result",false);
      result.put("message",id+" 질문 삭제에 실패했습니다.");
    }
    return ResponseEntity.ok(result);
  }
  @PreAuthorize("hasAuthority('MANAGER')")
  @DeleteMapping("/qna/answer/{id}")
  public ResponseEntity<?> deleteForceAnswer(
      @PathVariable("id") Long id){
    Map<String, Object> result = new HashMap<>();
    if(managementService.deleteQnaAnswer(id)){
      result.put("result",true);
      result.put("message",id + " 답변이 삭제되었습니다");
    }else{
      result.put("result",false);
      result.put("message",id + " 답변이 삭제에 실패했습니다.");
    }
    return ResponseEntity.ok(result);
  }

  @PreAuthorize("hasAuthority('MANAGER')")
  @GetMapping("/review")
  public ResponseEntity<?> getReview(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size){
    Map<String, Object> result = new HashMap<>();
    Page<ReviewDTO> reviewDTOS= managementService.getReviewPage(page,size);
    if(reviewDTOS != null && reviewDTOS.getTotalElements() > 0){
      result.put("result", true);
      result.put("message", "리뷰 목록입니다.");
      result.put("reviewList", reviewDTOS.getContent());
      result.put("totalPages", reviewDTOS.getTotalPages());
      result.put("currentPage", reviewDTOS.getNumber());
    }else{
      result.put("result", false);
      result.put("message", "리뷰 목록이 없습니다.");
    }
    return ResponseEntity.ok(result);
  }
  @PreAuthorize("hasAuthority('MANAGER')")
  @DeleteMapping("/review/{id}")
  public ResponseEntity<?> deleteForceReview(
      @PathVariable("id") Long id){
    Map<String, Object> result = new HashMap<>();
    if(managementService.deleteReview(id)){
      result.put("result", true);
      result.put("message", "리뷰를 삭제했습니다");
    }else{
      result.put("result", false);
      result.put("message", "리뷰 삭제에 실패했습니다");
    }
    return ResponseEntity.ok(result);
  }

  @PreAuthorize("hasAuthority('MANAGER')")
  @GetMapping("/reservation")
  public ResponseEntity<?> getReservation(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size){
    Map<String, Object> result = new HashMap<>();
    Page<ReservationDTO> reservationDTOS= managementService.getReservation(page, size);
    if(reservationDTOS != null && reservationDTOS.getTotalElements()>0){
      result.put("result", true);
      result.put("message", "예약 목록입니다.");
      result.put("reservationList", reservationDTOS.getContent());
      result.put("totalPages", reservationDTOS.getTotalPages());
      result.put("currentPage", reservationDTOS.getNumber());
    }else{
      result.put("result", false);
      result.put("message", "예약 목록이 없습니다.");
    }
    return ResponseEntity.ok(result);
  }
  @PreAuthorize("hasAuthority('MANAGER')")
  @GetMapping("/payment")
  public ResponseEntity<?> getPayment(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size){
    Map<String, Object> result = new HashMap<>();
    Page<PaymentDTO> paymentDTOS= managementService.getPayment(page, size);
    if(paymentDTOS!=null && paymentDTOS.getTotalElements()>0){
      result.put("result", true);
      result.put("message", "결제 목록입니다.");
      result.put("payList", paymentDTOS.getContent());
      result.put("totalPages", paymentDTOS.getTotalPages());
      result.put("currentPage", paymentDTOS.getNumber());
    }else{
      result.put("result", false);
      result.put("message", "결제 목록이 없습니다.");
    }
    return ResponseEntity.ok(result);
  }
}
