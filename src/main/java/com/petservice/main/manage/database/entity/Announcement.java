package com.petservice.main.manage.database.entity;

import com.petservice.main.common.database.entity.TimeEntity;
import com.petservice.main.user.database.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "announcement")
@Getter
@Setter
public class Announcement extends TimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 200, unique = true)
  private String title;

  @Column(nullable = false)
  private String type;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String content;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Priority priority=Priority.GENERAL;

  @ManyToOne(optional = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

}
