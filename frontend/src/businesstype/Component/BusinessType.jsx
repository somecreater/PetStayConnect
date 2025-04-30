import React from "react";
import CusomP from '../../common/Ui/CusomP';
import CustomLabel from "../../common/Ui/CustomLabel";
function BusinessType(props){
  
  const {id, sectorCode, typeCode, typeName, description} = props;
  return (
    <>
    <div className="BusinessType">

      <CustomLabel classtext={'BusinessTypeLabel'} title={'아이디'} for={'BusinessTypeInfo'}/>
      <CusomP classtext={'BusinessTypeInfo'} title={id}/>
      
      <CustomLabel classtext={'BusinessTypeLabel'} title={'타입 이름'} for={'BusinessTypeInfo'}/>
      <CusomP classtext={'BusinessTypeInfo'} title={typeName}/>

      <CustomLabel classtext={'BusinessTypeLabel'} title={'섹터 코드'} for={'BusinessTypeInfo'}/>
      <CusomP classtext={'BusinessTypeInfo'} title={sectorCode}/>

      <CustomLabel classtext={'BusinessTypeLabel'} title={'타입 코드'} for={'BusinessTypeInfo'}/>
      <CusomP classtext={'BusinessTypeInfo'} title={typeCode}/>

      <CustomLabel classtext={'BusinessTypeLabel'} title={'타입 설명'} for={'BusinessTypeInfo'}/>
      <CusomP classtext={'BusinessTypeInfo'} title={description}/>
    
    </div>
    </>
  );
}

export default BusinessType;