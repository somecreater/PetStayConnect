import React, { useState } from "react";
import CusomP from '../../common/Ui/CusomP';
import CustomLabel from "../../common/Ui/CustomLabel";
import TextInput from "../../common/Ui/TextInput";
import Button from "../../common/Ui/Button";
import ApiService from "../../common/Api/ApiService";
import Modal from '../../common/Ui/Modal';  

function BusinessTypeUpdateForm(props){
  const {exId,exTypename,exDescription,exSectorCode,exTypeCode,onSuccess}=props;

  const [isModalOpen,setModalOpen]=useState(false);
  const [typename,setTypename]= useState(exTypename);
  const [description,setDescription]= useState(exDescription);

  const handleTypeSubmit = async(e) => {
    e.preventDefault();
    const typedto={
      id: exId,
      typeName: typename,
      sectorCode: exSectorCode,
      typeCode: exTypeCode,
      description: description,
      petBusinessDTOList: []
    }

    const response = await ApiService.businessTypeService.update(exId,typedto);
    const result=response.data;
    if(result.result){
      const newtype=result.type;

      alert(result.message
        + '\n 새롭게 수정된 타입 정보'
        + '\n 타입 아이디: '+ newtype.id
        + '\n 타입 이름: '+ newtype.typeName
        + '\n 섹터 코드: '+ newtype.sectorCode
        + '\n 타입 코드: '+ newtype.typeCode
        + '\n 타입 설명: '+ newtype.description);
      setModalOpen(false);
      onSuccess();
    }else{
      alert(result.message);
    }
  }
  
  return (
    <>
      <form className="BusinessTypeForm" onSubmit={handleTypeSubmit}>
        
        <CusomP
          classtext={'alertText'}
          title={'타입 수정 창'}
        />
        <CusomP
          classtext={'alertText'}
          title={'섹터 코드와 타입 코드는 수정이 불가능합니다.'}
        />
        <CustomLabel classtetxt={'BusinessTypeLabel'} title={'타입 아이디:'} for={'BusinessTypeInfo'}/>
        <CusomP
          classtext={'BusinessTypeInfo'}
          title={exId}
        />
        <br/>

        <CustomLabel classtetxt={'BusinessTypeLabel'} title={'타입 이름:'} for={'UserUpdateInfo'}/>
        <TextInput 
          classtext="BusinessTypeInput"
          name="typeName" 
          value={typename} 
          placeholderText="새로운 사업자 타입 이름을 입력하세요" 
          onChange={(e)=>setTypename(e.target.value)}
        />
        <br/>

        <CustomLabel classtetxt={'BusinessTypeLabel'} title={'섹터 코드:'} for={'BusinessTypeInfo'}/>
        <CusomP
          classtext={'BusinessTypeInfo'}
          title={exSectorCode}
        />
        
        <CustomLabel classtetxt={'BusinessTypeLabel'} title={'타입 코드:'} for={'BusinessTypeInfo'}/>
        <CusomP
          classtext={'BusinessTypeInfo'}
          title={exTypeCode}
        />

        <CustomLabel classtetxt={'BusinessTypeLabel'} title={'타입 설명:'} for={'UserUpdateInfo'}/>
        <textarea className="" name="" value={description} 
        placeholder="사업자 타입에 대한 설명을 작성해 주세요." onChange={(e)=>setDescription(e.target.value)}/>

        <Button 
          classtext={'BusinessTypeButton'}
          type="button"
          title={'사업 타입 수정'}
          onClick={()=>setModalOpen(true)}
        />

        <Modal isOpen={isModalOpen} onClose={() => setModalOpen(false)}>
          <CusomP classtext={'alertText'} title={'정말로 수정 하시겠습니까?'}/>
          <Button
            classtext={'BusinessTypeButton'}
            type="submit"
            title={'타입 수정'}
            onClick={handleTypeSubmit}
          />
        </Modal>
      </form>
    </>
  );
}

export default BusinessTypeUpdateForm;
