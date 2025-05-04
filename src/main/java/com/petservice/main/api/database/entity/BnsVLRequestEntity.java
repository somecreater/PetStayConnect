package com.petservice.main.api.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Table(name = "bns_request")
@Entity
@Getter
@Setter
public class BnsVLRequestEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** 사업자등록번호 (10자리 숫자, '-' 제거) */
  @Column(name = "b_no", nullable = false, unique = true)
  private String bNo;

  /** 개업일자 (YYYYMMDD, '-' 제거) */
  @Column(name = "start_dt", nullable = false)
  private String startDt;

  /** 대표자명 (외국인은 영문) */
  @Column(name = "p_nm", nullable = false)
  private String pNm;

  /** 대표자명2 (외국인일 경우 한글명) */
  @Column(name = "p_nm2")
  private String pNm2;

  /** 상호 */
  @Column(name = "b_nm")
  private String bNm;

  /** 법인등록번호 */
  @Column(name = "corp_no")
  private String corpNo;

  /** 업태 */
  @Column(name = "b_sector")
  private String bSector;

  /** 종목 */
  @Column(name = "b_type")
  private String bType;

  /** 사업장주소 (선택) */
  @Column(name = "b_adr")
  private String bAdr;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private RequestStatus status;
}
