package com.petservice.main.manage.controller;


import com.petservice.main.manage.database.dto.AnnouncementDTO;
import com.petservice.main.manage.service.AnnouncementServiceInterface;
import com.petservice.main.user.database.dto.CustomUserDetails;
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

  @PreAuthorize("hasRole('MANAGER')")
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

  @PreAuthorize("hasRole('MANAGER')")
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

  @PreAuthorize("hasRole('MANAGER')")
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

}
