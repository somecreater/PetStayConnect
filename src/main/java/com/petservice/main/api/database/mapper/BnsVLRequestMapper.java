package com.petservice.main.api.database.mapper;

import com.petservice.main.api.database.dto.BnsVLRequest;
import com.petservice.main.api.database.entity.BnsVLRequestEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class BnsVLRequestMapper {

  //DB 저장용
  public BnsVLRequestEntity toEntity(BnsVLRequest bnsVLRequest){

    BnsVLRequestEntity entity =  new BnsVLRequestEntity();
    entity.setId(null);
    entity.setBNo(bnsVLRequest.getBNo());
    entity.setStartDt(bnsVLRequest.getStartDt());
    entity.setPNm(bnsVLRequest.getPNm());
    entity.setPNm2(bnsVLRequest.getPNm2());
    entity.setBNm(bnsVLRequest.getBNm());
    entity.setCorpNo(bnsVLRequest.getCorpNo());
    entity.setBSector(bnsVLRequest.getBSector());
    entity.setBType(bnsVLRequest.getBType());
    entity.setBAdr(bnsVLRequest.getBAdr());
    entity.setStatus(null);

    return entity;
  }

  public BnsVLRequest toDTO(BnsVLRequestEntity bnsVLRequest){

    BnsVLRequest DTO = new BnsVLRequest();
    DTO.setBNo(bnsVLRequest.getBNo());
    DTO.setStartDt(bnsVLRequest.getStartDt());
    DTO.setPNm(bnsVLRequest.getPNm());
    DTO.setPNm2(bnsVLRequest.getPNm2());
    DTO.setBNm(bnsVLRequest.getBNm());
    DTO.setCorpNo(bnsVLRequest.getCorpNo());
    DTO.setBSector(bnsVLRequest.getBSector());
    DTO.setBType(bnsVLRequest.getBType());
    DTO.setBAdr(bnsVLRequest.getBAdr());

    return  DTO;
  }
}
