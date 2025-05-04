package com.petservice.main.api.database.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BnsVLResponse {

  /** 요청 성공 시 "OK", 실패 시 오류 코드 */
  @JsonProperty("status_code")
  private String statusCode;

  /** 상태 메시지 (에러 발생 시 상세 메시지 포함) */
  @JsonProperty("status_message")
  private String statusMessage;

  /** 요청 건수 */
  @JsonProperty("request_cnt")
  private int requestCnt;

  /** 유효(valid)로 판정된 건수 */
  @JsonProperty("valid_cnt")
  private int validCnt;

  /** 개별 검증 결과 리스트 */
  private List<BnsVLResult> data;
}
