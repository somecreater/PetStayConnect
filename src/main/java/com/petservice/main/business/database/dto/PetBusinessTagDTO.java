package com.petservice.main.business.database.dto;

import com.petservice.main.business.database.entity.TagType;
import lombok.Data;

@Data
public class PetBusinessTagDTO {

  private Long id;
  private String tagName;
  private TagType tagType;
  private Long business_id;
  private String business_name;
}
