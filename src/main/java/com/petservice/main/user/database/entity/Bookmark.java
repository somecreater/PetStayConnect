package com.petservice.main.user.database.entity;

import com.petservice.main.common.database.entity.TimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Table(name = "book_marks")
@Entity
@Getter
@Setter
public class Bookmark extends TimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "book_mark_type")
  private BookmarkType bookmarkType;

  @Column(name = "target_id")
  private Long targetId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

}
