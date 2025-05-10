import React from 'react';
import CusomP from '../../common/Ui/CusomP';

function Business({businesssDTO}){
  
    const { businessName, status, minPrice, maxPrice, facilities, description, avgRate,
        petBusinessTypeName, registrationNumber  } = businesssDTO;
  
  return(
    <>
      <div className='Business'>
        <label className='BusinessLabel' htmlFor='BusinessInfo'>사업체 이름</label>
        <CusomP classtext={'BusinessInfo'} title={businessName}/>
        <label className='BusinessLabel' htmlFor='BusinessInfo'>사업체 상태</label>
        <CusomP classtext={'BusinessInfo'} title={status}/>
        <label className='BusinessLabel' htmlFor='BusinessInfo'>최소 가격</label>
        <CusomP classtext={'BusinessInfo'} title={minPrice}/>
        <label className='BusinessLabel' htmlFor='BusinessInfo'>최대 가격</label>
        <CusomP classtext={'BusinessInfo'} title={maxPrice}/>
        <label className='BusinessLabel' htmlFor='BusinessInfo'>편의시설</label>
        <CusomP classtext={'BusinessInfo'} title={facilities}/>
        <label className='BusinessLabel' htmlFor='BusinessInfo'>설명</label>
        <CusomP classtext={'BusinessInfo'} title={description}/>
        <label className='BusinessLabel' htmlFor='BusinessInfo'>평점</label>
        <CusomP classtext={'BusinessInfo'} title={avgRate}/>
        <label className='BusinessLabel' htmlFor='BusinessInfo'>서비스 타입</label>
        <CusomP classtext={'BusinessInfo'} title={petBusinessTypeName}/>
        <label className='BusinessLabel' htmlFor='BusinessInfo'>사업자 등록번호</label>
        <CusomP classtext={'BusinessInfo'} title={registrationNumber}/>
      </div>
    </>
  );
}

export default Business;