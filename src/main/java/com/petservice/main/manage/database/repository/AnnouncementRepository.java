package com.petservice.main.manage.database.repository;

import com.petservice.main.manage.database.entity.Announcement;
import com.petservice.main.manage.database.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement,Long> {

  List<Announcement> findByPriority(Priority priority);
}
