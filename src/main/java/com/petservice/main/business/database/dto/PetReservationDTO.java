package com.petservice.main.business.database.dto;

import lombok.Data;
@Data

public class PetReservationDTO {

    private Long id;
    private Long petId;
    private Long reservationId;
}
