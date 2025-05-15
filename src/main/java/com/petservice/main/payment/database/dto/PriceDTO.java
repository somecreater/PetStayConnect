package com.petservice.main.payment.database.dto;


import com.petservice.main.business.database.dto.PetBusinessDTO;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PriceDTO {

  private Long id;
  private BigDecimal amount;
  private String type;
  private PetBusinessDTO petBusinessDTO;

}
