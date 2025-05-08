package com.petservice.main.api.controller;

import com.petservice.main.api.database.dto.BnsVLRequest;
import com.petservice.main.api.service.Interface.BnsVLServiceInterface;
import com.petservice.main.user.database.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/validation")
public class ApiController {
  private final BnsVLServiceInterface bnsVLService;

  @PostMapping("/business")
  public ResponseEntity<?> sendValidationRequest(
      @AuthenticationPrincipal CustomUserDetails principal,
      @RequestBody BnsVLRequest bnsVLRequest
  ){
    Map<String,Object> result=new HashMap<>();
    BnsVLRequest request= bnsVLService.RegisterVLRequest(bnsVLRequest);

    if(request != null){
      result.put("result",true);
      result.put("message","요청이 정상적으로 저장되었습니다. 최소 1시간 이상 걸립니다!!");
      result.put("request",request);
    }else{
      result.put("result",true);
      result.put("message","요청을 보내는 것에 실패했습니다. " +
          "이미 요청이 존재하거나, 서버상 문제일수도 있습니다!!");
    }

    return ResponseEntity.ok(result);
  }

}
