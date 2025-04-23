import React from 'react';
import { useUser } from '../../common/Context/UserContext';
import CustomLabel from '../../common/Ui/CustomLabel';
import CusomP from '../../common/Ui/CusomP';
import '../../common/Css/common.css';

function UserProviderInfo({petBusinessDTO}){
  const {
    businessName,
    status,
    minPrice,
    maxPrice,
    facilities,
    description,
    avgRate,
    registrationNumber,
    bankAccount,
    varification,
  } = petBusinessDTO;

  return(
    <>
    <div className='UserProviderInfo'>
      <CustomLabel classtetxt={'ProviderInfolabel'} title={'사업체 이름:'} for={'ProviderInfo'}/> 
      <CusomP classtext={'ProviderInfo'} title={businessName}/>

      <CustomLabel classtetxt={'ProviderInfolabel'} title={'현재 상태:'} for={'ProviderInfo'}/> 
      <CusomP classtext={'ProviderInfo'} title={status}/>
      
      <CustomLabel classtetxt={'ProviderInfolabel'} title={'최소가격:'} for={'ProviderInfo'}/> 
      <CusomP classtext={'ProviderInfo'} title={minPrice}/>
      
      <CustomLabel classtetxt={'ProviderInfolabel'} title={'최대가격:'} for={'ProviderInfo'}/> 
      <CusomP classtext={'ProviderInfo'} title={maxPrice}/>

      <CusomP classtext={'ProviderInfo'} title={'서비스 요약란'}/>
      <textarea className='ProviderLongInfo'>
        {facilities}
      </textarea>
      
      <CusomP classtext={'ProviderInfo'} title={'서비스 설명란'}/>
      <textarea className='ProviderLongInfo'>
        {description}
      </textarea>

      <br/>
            
      <CustomLabel classtetxt={'ProviderInfolabel'} title={'평점:'} for={'ProviderInfo'}/> 
      <CusomP classtext={'ProviderInfo'} title={avgRate}/>

      
      <CustomLabel classtetxt={'ProviderInfolabel'} title={'사업자 번호:'} for={'ProviderInfo'}/> 
      <CusomP classtext={'ProviderInfo'} title={registrationNumber}/>

      
      <CustomLabel classtetxt={'ProviderInfolabel'} title={'계좌번호:'} for={'ProviderInfo'}/> 
      <CusomP classtext={'ProviderInfo'} title={bankAccount}/>

      
      <CustomLabel classtetxt={'ProviderInfolabel'} title={'인증현황:'} for={'ProviderInfo'}/> 
      <CusomP classtext={'ProviderInfo'} title={varification}/>
    </div>
    </>
  );
}

export default UserProviderInfo;