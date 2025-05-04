package com.petservice.main.api.database.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BnsVLResult {
  @JsonProperty("b_no")
  private String bNo;

  @JsonProperty("start_dt")
  private String startDt;

  @JsonProperty("p_nm")
  private String pNm;

  @JsonProperty("p_nm2")
  private String pNm2;

  @JsonProperty("b_nm")
  private String bNm;

  @JsonProperty("corp_no")
  private String corpNo;

  @JsonProperty("b_sector")
  private String bSector;

  @JsonProperty("b_type")
  private String bType;

  @JsonProperty("b_adr")
  private String bAdr;

  /** 진위여부 코드 ("01": 유효, "02": 유효하지 않음) */
  private String valid;

  /** 유효하지 않을 때 반환되는 메시지 */
  @JsonProperty("valid_msg")
  private String validMsg;
}
