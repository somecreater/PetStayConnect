package com.petservice.main.manage.service;

import com.petservice.main.manage.database.dto.AnnouncementDTO;
import com.petservice.main.manage.database.entity.Announcement;
import com.petservice.main.manage.database.entity.Priority;
import com.petservice.main.manage.database.mapper.AnnouncementMapper;
import com.petservice.main.manage.database.repository.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnnouncementService implements AnnouncementServiceInterface{

  private final AnnouncementRepository announcementRepository;
  private final AnnouncementMapper announcementMapper;

  @Override
  @Transactional(readOnly = true)
  public Page<AnnouncementDTO> announcementList(int page, int size) {
    try {
      Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
      return announcementRepository.findAll(pageable).map(announcementMapper::toDTO);

    }catch (Exception e){
      log.info("공지가 비어있거나 가져오는 도중 오류가 발생하였습니다.");
      e.printStackTrace();
      return null;
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<AnnouncementDTO> PriorityAnnouncementList() {
    try {
      List<Announcement> announcementDTOS = announcementRepository.findByPriority(Priority.SPECIAL);
      return announcementDTOS.stream().map(announcementMapper::toDTO).toList();
    }catch (Exception e){
      log.info("특별 공지가 비어있거나 가져오는 도중 오류가 발생하였습니다.");
      e.printStackTrace();
      return null;
    }
  }

  @Override
  @Transactional(readOnly = true)
  public AnnouncementDTO getAnnouncement(Long id) {
    Announcement announcement=announcementRepository.findById(id).orElse(null);
    if(announcement == null){
      return null;
    }
    return announcementMapper.toDTO(announcement);
  }

  @Override
  @Transactional
  public AnnouncementDTO registerAnnouncement(AnnouncementDTO announcementDTO) {
    try {
      if (!ValidationAnnouncement(announcementDTO)) {
        throw new IllegalArgumentException("공지를 다시 입력해주세요.");
      }
      Announcement announcement = announcementMapper.toEntity(announcementDTO);
      Announcement insert = announcementRepository.save(announcement);

      return announcementMapper.toDTO(insert);
    }catch (Exception e){
      log.info("공지 등록을 실패하였습니다.");
      e.printStackTrace();
      return null;
    }
  }

  @Override
  @Transactional
  public AnnouncementDTO updateAnnouncement(AnnouncementDTO announcementDTO) {
    try {
      Announcement exAnnouncement =
          announcementRepository.findById(announcementDTO.getId()).orElse(null);
      if (exAnnouncement == null) {
        return null;
      }
      if (!ValidationAnnouncement(announcementDTO)) {
        return null;
      }
      exAnnouncement.setTitle(announcementDTO.getTitle());
      exAnnouncement.setType(announcementDTO.getType());
      exAnnouncement.setContent(announcementDTO.getContent());
      exAnnouncement.setPriority(announcementDTO.getPriority());
      exAnnouncement.setUpdatedAt(LocalDateTime.now());

      Announcement update = announcementRepository.save(exAnnouncement);

      return announcementMapper.toDTO(update);
    }catch (Exception e){
      log.info("공지 수정에 실패하였습니다. 제목이나 내용을 다시 확인해주세요!");
      e.printStackTrace();
      return null;
    }
  }

  @Override
  @Transactional
  public boolean deleteAnnouncement(Long id) {
    Announcement exAnnouncement= announcementRepository.findById(id).orElse(null);
    try {
      if (exAnnouncement == null) {
        return false;
      }
      announcementRepository.deleteById(id);
      return true;
    }catch (Exception e){
      log.info("정상적으로 공지가 삭제되지 않았습니다.");
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean ValidationAnnouncement(AnnouncementDTO announcementDTO){

    if(announcementDTO == null){
      return false;
    }

    if(isBlank(announcementDTO.getTitle())
    || isBlank(announcementDTO.getType())
    || isBlank(announcementDTO.getContent())
    || announcementDTO.getPriority() == null){
      return false;
    }

    return true;
  }

  @Override
  public boolean isBlank(String str){
    return str == null || str.trim().isEmpty();
  }
}
