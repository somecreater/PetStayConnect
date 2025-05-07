package com.petservice.main.business.database.dto;

import com.petservice.main.user.database.dto.PetDTO;
import lombok.Data;
@Data

public class PetReservationDTO {

    private Long id;
    private Long petId;
    private Long reservationId;
    private PetDTO petDTO;
    private ReservationDTO reservationDTO;
}
