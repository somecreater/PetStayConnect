package com.petservice.main.user.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "address")
@Entity
@Getter
@Setter
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name ="user_id")
  private Long userId;

  @Column(name = "user_login_id")
  private String userLoginId;

  @Column(name = "cor_x")
  private Double corX;

  @Column(name = "cor_y")
  private Double corY;

  @Column(name="road_address")
  private String roadAddress;

  @Column(name="detail_address")
  private String detailAddress;

  @Column(name="postal_address")
  private String postalAddress;

  @Column
  private String province;

  @Column
  private String city;

  @Column
  private String town;

}
