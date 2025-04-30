package com.petservice.main.business.database.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PetBusinessTypeDTO {

  private Long id;
  private String typeName;
  private String description;
  private String sectorCode;
  private String typeCode;
  private List<PetBusinessDTO> petBusinessDTOList = new ArrayList<>();
}
