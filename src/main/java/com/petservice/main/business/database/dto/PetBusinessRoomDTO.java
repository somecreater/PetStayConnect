package com.petservice.main.business.database.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PetBusinessRoomDTO {

    private Long id;
    private Long petBusinessId;
    private String roomType;
    private String description;
    private Integer roomCount;
    private List<ReservationDTO> reservationDTOList = new ArrayList<>();
}
