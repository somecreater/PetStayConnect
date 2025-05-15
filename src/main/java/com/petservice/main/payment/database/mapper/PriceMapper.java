package com.petservice.main.payment.database.mapper;


import com.petservice.main.business.database.mapper.PetBusinessMapper;
import com.petservice.main.payment.database.dto.PriceDTO;
import com.petservice.main.payment.database.entity.Price;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PriceMapper {

  private final PetBusinessMapper petBusinessMapper;

  public Price toEntity(PriceDTO priceDTO){
    Price price=new Price();
    price.setId(priceDTO.getId());
    price.setType(priceDTO.getType());
    price.setAmount(priceDTO.getAmount());
    if(priceDTO.getPetBusinessDTO()!=null) {
      price.setPetBusiness(petBusinessMapper.toEntity(priceDTO.getPetBusinessDTO()));
    }
    return price;
  }

  public PriceDTO toDto(Price price){
    PriceDTO priceDTO=new PriceDTO();
    priceDTO.setId(price.getId());
    priceDTO.setType(price.getType());
    priceDTO.setAmount(price.getAmount());
    if(priceDTO.getPetBusinessDTO()!=null) {
      priceDTO.setPetBusinessDTO(petBusinessMapper.toBasicDTO(price.getPetBusiness()));
    }
    return priceDTO;
  }
}
