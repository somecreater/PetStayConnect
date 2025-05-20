package com.petservice.main.payment.controller;

import com.petservice.main.payment.database.dto.PaymentCancelRequestDTO;
import com.petservice.main.payment.database.dto.PaymentDTO;
import com.petservice.main.payment.database.dto.PaymentRequestDTO;
import com.petservice.main.payment.database.entity.PaymentStatus;
import com.petservice.main.payment.service.PaymentServiceInterface;
import com.petservice.main.user.database.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PayController {

  private final PaymentServiceInterface paymentService;

  @PostMapping
  public ResponseEntity<?> registerPayment(
      @AuthenticationPrincipal CustomUserDetails principal,
      @RequestBody PaymentRequestDTO requestDTO
  ){
    Map<String, Object> response=new HashMap<>();
    PaymentDTO paymentDTO=paymentService.RegisterPayment(requestDTO);

    if(paymentDTO != null){
      response.put("result", true);
      response.put("message", "결제가 완료되었습니다. 만약 가상 계좌 수단일 경우," +
          "나중에 결제 됩니다.");
      response.put("paymentResult","결제 방법: " + paymentDTO.getPaymentMethod()
          + " 결제 상태: " + paymentDTO.getPaymentStatus()
          + " 결제 UID: " + paymentDTO.getImpUid());
      response.put("payment", paymentDTO);
    }else{
      response.put("result", false);
      //추후 문의 게시판으로 따로 처리
      response.put("message","알수 없는 오류로 결제가 비정상 처리 되었습니다." +
          "만약 금전적 손실이 있을 경우 문의 게시판이나, " +
          "아래의 번호를 통해 문의해 주세요.");
    }

    return ResponseEntity.ok(response);
  }

  //메일 전송을 통해 알림(추후 수정)
  @PutMapping("/webhook")
  public ResponseEntity<?> VBankPaymentUpdate(
      @RequestBody Map<String, Object> webhookPayload){
    Map<String, Object> response=new HashMap<>();

    String impUid      = (String) webhookPayload.get("imp_uid");
    String status      = (String) webhookPayload.get("status");
    PaymentDTO paymentDTO = paymentService.RegisterVBankPayment(impUid,status);

    if(paymentDTO.getPaymentStatus() == PaymentStatus.PAID){
      response.put("result",true);
      response.put("message","가상계좌 결제가 정상처리 되었습니다.");
      response.put("payment",paymentDTO);
    }else{
      response.put("result",false);
      response.put("message","가상계좌 결제가 비정상적으로 처리되었습니다." +
          " 만약 금액이 환불이 안됐다면, 문의 게시판이나 아래의 번호로 문의주세요");
    }
    return ResponseEntity.ok(response);
  }

  @GetMapping("/business")
  public ResponseEntity<?> BusinessList(
      @AuthenticationPrincipal CustomUserDetails principal,
      @RequestParam Long business_id,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size
  ){
    Map<String, Object> response=new HashMap<>();
    Page<PaymentDTO> paymentDTOS=paymentService.PaymentListByBusiness(
        business_id, page, size);

    if(paymentDTOS != null){
      response.put("result", true);
      response.put("message","결제 내역입니다(사업자)");
      response.put("list", paymentDTOS.getContent());
      response.put("totalPages", paymentDTOS.getTotalPages());
    }else{
      response.put("result", false);
      response.put("message","결제 내역을 가져오는 것에 실패하였습니다(사업자)");
    }
    return ResponseEntity.ok(response);
  }

  @GetMapping("/user")
  public ResponseEntity<?> UserList(
      @AuthenticationPrincipal CustomUserDetails principal,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size){
    Map<String, Object> response=new HashMap<>();
    Page<PaymentDTO> paymentDTOS=paymentService.PaymentListByUser(principal.getUsername(), 
        page, size);

    if(paymentDTOS != null){
      response.put("result", true);
      response.put("message","결제 내역입니다(사용자)");
      response.put("list", paymentDTOS.getContent());
      response.put("totalPages", paymentDTOS.getTotalPages());
    }else{
      response.put("result", false);
      response.put("message","결제 내역을 가져오는 것에 실패하였습니다(사용자)");
    }
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{payment_id}")
  public ResponseEntity<?> detailPayment(
      @AuthenticationPrincipal CustomUserDetails principal,
      @PathVariable("payment_id") Long payment_id){
    Map<String, Object> response=new HashMap<>();
    PaymentDTO paymentDTO=paymentService.getPayment(payment_id);

    if(paymentDTO != null){
      response.put("result", true);
      response.put("message", "결제 내용을 가져왔습니다.");
      response.put("payment", paymentDTO);
    }else{
      response.put("result", false);
      response.put("message", "결제 내용이 없거나, 가져오는 것을 실패하였습니다.");
    }
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{payment_id}")
  public ResponseEntity<?> deletePayment(
      @AuthenticationPrincipal CustomUserDetails principal,
      @PathVariable("payment_id") Long payment_id,
      @RequestBody PaymentCancelRequestDTO requestDTO) {
    Map<String, Object> response=new HashMap<>();
    PaymentDTO paymentDTO=paymentService.CancelPayment(requestDTO);

    if(paymentDTO != null){
      response.put("result", true);
      response.put("message", "결제가 취소되었습니다.");
    }else{
      response.put("result", false);
      response.put("message", "결제 취소에 실패하였습니다.");
    }
    return ResponseEntity.ok(response);
  }

}
