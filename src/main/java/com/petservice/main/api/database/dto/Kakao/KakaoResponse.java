package com.petservice.main.api.database.dto.Kakao;

import lombok.Data;

import java.util.List;

@Data
public class KakaoResponse {
  private Meta meta;
  private List<Document> documents;
}
