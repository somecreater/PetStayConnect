package com.petservice.main.api.database.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BnsVLRequestList {
  @JsonProperty("businesses")
  private List<BnsVLRequest> businesses;
}
