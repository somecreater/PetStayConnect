package com.petservice.main.manage.database.mapper;

import com.petservice.main.manage.database.dto.AnnouncementDTO;
import com.petservice.main.manage.database.entity.Announcement;
import com.petservice.main.user.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnnouncementMapper {

  private final UserRepository userRepository;

  public Announcement toEntity(AnnouncementDTO dto){
    Announcement announcement= new Announcement();
    announcement.setId(dto.getId());
    announcement.setTitle(dto.getTitle());
    announcement.setType(dto.getType());
    announcement.setContent(dto.getContent());
    announcement.setPriority(dto.getPriority());
    announcement.setCreatedAt(dto.getCreatedAt());
    announcement.setUpdatedAt(dto.getUpdatedAt());
    if(dto.getUserId() != null){
      announcement.setUser(
          userRepository.findById(dto.getUserId()).orElse(null));
    }else if(dto.getUserLoginId() != null){
      announcement.setUser(
          userRepository.findByUserLoginId(dto.getUserLoginId()).orElse(null));
    }

    return announcement;
  }

  public AnnouncementDTO toDTO(Announcement entity){
    AnnouncementDTO announcementDTO= new AnnouncementDTO();
    announcementDTO.setId(entity.getId());
    announcementDTO.setTitle(entity.getTitle());
    announcementDTO.setType(entity.getType());
    announcementDTO.setContent(entity.getContent());
    announcementDTO.setPriority(entity.getPriority());
    announcementDTO.setCreatedAt(entity.getCreatedAt());
    announcementDTO.setUpdatedAt(entity.getUpdatedAt());
    if(entity.getUser() != null){
      announcementDTO.setUserId(entity.getUser().getId());
      announcementDTO.setUserLoginId(entity.getUser().getUserLoginId());
    }

    return announcementDTO;
  }

}
