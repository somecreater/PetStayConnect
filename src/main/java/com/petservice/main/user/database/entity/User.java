package com.petservice.main.user.database.entity;

import com.petservice.main.business.database.entity.PetBusiness;
import com.petservice.main.common.database.entity.TimeEntity;
import com.petservice.main.qna.database.entity.QnaAnswer;
import com.petservice.main.qna.database.entity.QnaPost;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends TimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_login_id", unique = true)
  private String userLoginId;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @Column(unique = true)
  private String phone;

  @Enumerated(EnumType.STRING)
  @Column(name="login_type",nullable = false)
  private UserType loginType;

  @Column(name = "pet_number", nullable = true)
  private Integer petNumber;

  @Column(name="qna_score", nullable = true)
  private Integer qnaScore;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Pet> petList=new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Bookmark> bookmarkList=new ArrayList<>();

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<QnaPost> qnaPostList=new ArrayList<>();

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<QnaAnswer> qnaAnswerList=new ArrayList<>();

  @OneToOne(mappedBy="user", optional=true)
  private PetBusiness petBusiness;

}
