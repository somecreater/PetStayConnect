package com.petservice.main.qna.database.entity;

import com.petservice.main.common.database.entity.TimeEntity;
import com.petservice.main.user.database.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Table(name = "qna_answer")
@Getter
@Setter
public class QnaAnswer extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private QnaPost post;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column
    public Integer score;

    @Column(name = "is_adopted")
    public Boolean isAdopted;
}
