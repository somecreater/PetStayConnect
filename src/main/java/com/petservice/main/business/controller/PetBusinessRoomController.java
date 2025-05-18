package com.petservice.main.business.controller;

import com.petservice.main.business.database.dto.PetBusinessRoomDTO;
import com.petservice.main.business.service.Interface.PetBusinessRoomServiceInterface;
import com.petservice.main.user.database.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class PetBusinessRoomController {

  private final PetBusinessRoomServiceInterface serviceInterface;

  @GetMapping("/{business_id}")
  public ResponseEntity<?> getRoomById(
      @AuthenticationPrincipal CustomUserDetails principal,
      @PathVariable("business_id") Long business_id){
    Map<String,Object> response=new HashMap<>();
    List<PetBusinessRoomDTO> rooms= serviceInterface.getRoomByBusinessId(business_id);

    if(rooms != null){
      response.put("result",true);
      response.put("message",rooms.size()+" 개의 방을 가져왔습니다.");
      response.put("rooms",rooms);
      return ResponseEntity.ok(response);

    }else{
      response.put("result",false);
      response.put("message","방이 존재하지 않거나 서버상 문제로 실패했습니다!");
      return ResponseEntity.ok(response);
    }
  }

  @GetMapping("/{business_id}/{room_type}")
  public ResponseEntity<?> getRoomByType(
      @AuthenticationPrincipal CustomUserDetails principal,
      @PathVariable("business_id") Long business_id,
      @PathVariable("room_type") String room_type){
    Map<String,Object> response=new HashMap<>();

    PetBusinessRoomDTO businessRoomDTO = serviceInterface.getRoomByType(business_id,room_type);

    if(businessRoomDTO!=null){
      response.put("result",true);
      response.put("room", businessRoomDTO);
      return ResponseEntity.ok(response);
    }else{
      response.put("result",false);
      response.put("message","방이 존재하지 않습니다.");
      return ResponseEntity.ok(response);
    }
  }

  @PutMapping("/{business_id}/{room_id}")
  public ResponseEntity<?> updateRoom(
      @AuthenticationPrincipal CustomUserDetails principal,
      @PathVariable("business_id") Long business_id,
      @PathVariable("room_id") Long room_id,
      @RequestBody PetBusinessRoomDTO petBusinessRoomDTO){
    Map<String,Object> response=new HashMap<>();

    PetBusinessRoomDTO businessRoomDTO=serviceInterface.updateRoom(
        business_id, room_id, petBusinessRoomDTO);

    if(businessRoomDTO != null){
      response.put("result",true);
      response.put("message",businessRoomDTO.getId()+" 방이 수정되었습니다.");
      response.put("updateRoom",businessRoomDTO);
    }else{
      response.put("result",false);
      response.put("message","방이 존재하지 않거나 서버상 문제로 실패했습니다!");
    }

    return ResponseEntity.ok(response);
  }

  @PostMapping("/{business_id}")
  public ResponseEntity<?> insertRoom(
      @AuthenticationPrincipal CustomUserDetails principal,
      @PathVariable("business_id") Long business_id,
      @RequestBody PetBusinessRoomDTO petBusinessRoomDTO){
    Map<String,Object> response=new HashMap<>();

    PetBusinessRoomDTO insertRoom=
        serviceInterface.createRoom(business_id, petBusinessRoomDTO);

    if(insertRoom != null){
      response.put("result",true);
      response.put("message","새로운 방이 입력되었습니다");
      response.put("room",insertRoom);
    }else{
      response.put("result",false);
      response.put("message","이미 존재하는 타입이거나 서버상 문제로 실패했습니다.");
    }

    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{business_id}/{room_id}")
  public ResponseEntity<?> deleteRoom(
      @AuthenticationPrincipal CustomUserDetails principal,
      @PathVariable("business_id") Long business_id,
      @PathVariable("room_id") Long room_id){
    Map<String,Object> response=new HashMap<>();

    if(serviceInterface.deleteRoom(business_id, room_id)){
      response.put("result",true);
      response.put("message","방이 삭제되었습니다.");
    }else{
      response.put("result",false);
      response.put("message","방 삭제에 실패했습니다");
    }

    return ResponseEntity.ok(response);
  }
}
