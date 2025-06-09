package com.petservice.main.manage.service;

import com.petservice.main.manage.database.dto.AnnouncementDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AnnouncementServiceInterface {

  public Page<AnnouncementDTO> announcementList(int page, int size);
  public List<AnnouncementDTO> PriorityAnnouncementList();
  public AnnouncementDTO getAnnouncement(Long id);
  public AnnouncementDTO registerAnnouncement(AnnouncementDTO announcementDTO);
  public AnnouncementDTO updateAnnouncement(AnnouncementDTO announcementDTO);
  public boolean deleteAnnouncement(Long id);
  public boolean ValidationAnnouncement(AnnouncementDTO announcementDTO);
  public boolean isBlank(String str);

}
