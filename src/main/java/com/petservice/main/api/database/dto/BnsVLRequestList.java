package com.petservice.main.api.database.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BnsVLRequestList {
  List<BnsVLRequest> requests;
}
