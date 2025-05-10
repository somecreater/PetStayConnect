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
    entity.setBno(bnsVLRequest.getBNo());
    entity.setStartDt(bnsVLRequest.getStartDt());
    entity.setPnm(bnsVLRequest.getPNm());
    entity.setPnm2(bnsVLRequest.getPNm2());
    entity.setBnm(bnsVLRequest.getBNm());
    entity.setCorpno(bnsVLRequest.getCorpNo());
    entity.setBsector(bnsVLRequest.getBSector());
    entity.setBtype(bnsVLRequest.getBType());
    entity.setBadr(bnsVLRequest.getBAdr());
    entity.setStatus(null);

    return entity;
  }

  public BnsVLRequest toDTO(BnsVLRequestEntity bnsVLRequest){

    BnsVLRequest DTO = new BnsVLRequest();
    DTO.setBNo(bnsVLRequest.getBno());
    DTO.setStartDt(bnsVLRequest.getStartDt());
    DTO.setPNm(bnsVLRequest.getPnm());
    DTO.setPNm2(bnsVLRequest.getPnm2());
    DTO.setBNm(bnsVLRequest.getBnm());
    DTO.setCorpNo(bnsVLRequest.getCorpno());
    DTO.setBSector(bnsVLRequest.getBsector());
    DTO.setBType(bnsVLRequest.getBtype());
    DTO.setBAdr(bnsVLRequest.getBadr());

    return  DTO;
  }
}
