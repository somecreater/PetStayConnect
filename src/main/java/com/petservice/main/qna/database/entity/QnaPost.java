package com.petservice.main.qna.database.entity;

import com.petservice.main.common.database.entity.TimeEntity;
import com.petservice.main.user.database.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "qna_post")
@Getter
@Setter
public class QnaPost extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column
    private String category;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;


}
