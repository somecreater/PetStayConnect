package com.petservice.main.api.database.dto.Kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Meta {
  @JsonProperty("total_count")
  private long totalCount;
  @JsonProperty("pageable_count")
  private long pageableCount;
  private boolean isEnd;
}
