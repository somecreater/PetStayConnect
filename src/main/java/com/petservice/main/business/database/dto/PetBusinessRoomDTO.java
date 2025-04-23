package com.petservice.main.business.database.dto;

import lombok.Data;
@Data
public class PetBusinessRoomDTO {

    private Long id;
    private Long petBusinessId;
    private String roomType;
    private String description;
    private Long roomCount;

}
