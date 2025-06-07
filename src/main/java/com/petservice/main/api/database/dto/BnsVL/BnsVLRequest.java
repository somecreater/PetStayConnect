package com.petservice.main.api.database.dto.BnsVL;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
사업자등록정보 진위확인 Request Body
(사업자 등록 번호, 개업 일자, 대표자 명은 필수)
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BnsVLRequest {
  /** 사업자등록번호 (10자리 숫자, '-' 제거) */
  @JsonProperty("b_no")
  private String bNo;

  /** 개업일자 (YYYYMMDD, '-' 제거) */
  @JsonProperty("start_dt")
  private String startDt;

  /** 대표자명 (외국인은 영문) */
  @JsonProperty("p_nm")
  private String pNm;

  /** 대표자명2 (외국인일 경우 한글명) */
  @JsonProperty("p_nm2")
  private String pNm2;

  /** 상호 */
  @JsonProperty("b_nm")
  private String bNm;

  /** 법인등록번호 */
  @JsonProperty("corp_no")
  private String corpNo;

  /** 업태 */
  @JsonProperty("b_sector")
  private String bSector;

  /** 종목 */
  @JsonProperty("b_type")
  private String bType;

  /** 사업장주소 (선택) */
  @JsonProperty("b_adr")
  private String bAdr;
}
