package com.petservice.main.payment.controller;

import com.petservice.main.payment.database.dto.AccountDTO;
import com.petservice.main.payment.service.AccountServiceInterface;
import com.petservice.main.user.database.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

  private final AccountServiceInterface accountService;

  @GetMapping("/list")
  public ResponseEntity<?> getList(
      @AuthenticationPrincipal CustomUserDetails principal,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size){
    Map<String, Object> result = new HashMap<>();

    Page<AccountDTO> accountDTOS=accountService.getAccountList(page,size);
    if(accountDTOS != null){
      result.put("result",true);
      result.put("message","사용자들의 전체 정산계좌 정보 목록을 가져옵니다.");
      result.put("list", accountDTOS);
    }else{
      result.put("result",false);
      result.put("message","정산계좌 목록이 존재하지 않거나 가져오는 것에 실패하였습니다.");
    }

    return ResponseEntity.ok(result);
  }

  @GetMapping("/user")
  public ResponseEntity<?> getUser(
      @AuthenticationPrincipal CustomUserDetails principal){
    Map<String, Object> result = new HashMap<>();
    AccountDTO accountDTO=accountService.getAccountByUserLoginId(principal.getUsername());

    if(accountDTO !=null){
      result.put("result",true);
      result.put("message","일반 회원의 정산계좌 정보를 가져옵니다.");
      result.put("account",accountDTO);
    }else{
      result.put("result",true);
      result.put("message","정산계좌 정보가 존재하지 않습니다.");
    }
    return ResponseEntity.ok(result);
  }

  @GetMapping("/business/{business_id}")
  public ResponseEntity<?> getBusiness(
      @AuthenticationPrincipal CustomUserDetails principal,
      @PathVariable("business_id") Long business_id){
    Map<String, Object> result = new HashMap<>();
    AccountDTO accountDTO=accountService.getAccountByBusinessId(business_id);

    if(accountDTO !=null){
      result.put("result",true);
      result.put("message","사업자의 정산계좌 정보를 가져옵니다.");
      result.put("account",accountDTO);
    }else{
      result.put("result",true);
      result.put("message","정산계좌 정보가 존재하지 않습니다.");
    }
    return ResponseEntity.ok(result);
  }
}
