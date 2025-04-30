import React, { useState } from "react";
import Button from '../../common/Ui/Button';
import Modal from '../../common/Ui/Modal';
import CusomP from '../../common/Ui/CusomP';
import ApiService from "../../common/Api/ApiService";

function BusinessTypeDeleteButton(props){
  const {type_id,businesstype}=props;
  const [isModalOpen, setModalOpen] = useState(false);

  const handleTypeDelete= async (type_id,businesstype) =>{
    const response= await ApiService.businessTypeService.delete(type_id,businesstype);
    const result = response.data;

    if(result.result){
      const deletetype=result.deleteType;
      alert(result.message+'\n 새로고침하면 사라집니다!!'
        +'\n 삭제된 타입 정보: \n'
        + '아이디: '+ deletetype.id
        + '타입 이름: '+ deletetype.typeName
        + '섹터 코드: '+ deletetype.sectorCode
        + '타입 코드: '+ deletetype.typeCode
        + '타입 설명: '+ deletetype.description);
        setModalOpen(false);

    }else{
        alert(result.message);
        setModalOpen(false);
    }

  }
  return (
    <>
    <div className="BusinessTypeComponent">
      <Button
        classtext={'BusinessTypeButton'}
        type="button"
        title={'사업 타입 삭제'}
        onClick={()=>setModalOpen(true)}
      />
      <Modal isOpen={isModalOpen} onClose={() => setModalOpen(false)}>
        <CusomP classtext={'alertText'} title={'정말로 삭제하겠습니까?'}/>
        <Button
          classtext={'BusinessTypeButton'}
          type="button"
          title={'타입 삭제(복구 불가)'}
          onClick={handleTypeDelete(type_id,businesstype)}
        />
      </Modal>
    </div>
    </>
  );
}

export default BusinessTypeDeleteButton;