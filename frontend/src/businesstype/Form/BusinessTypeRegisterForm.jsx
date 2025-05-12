import React, { useState } from "react";
import CusomP from '../../common/Ui/CusomP';
import CustomLabel from "../../common/Ui/CustomLabel";
import TextInput from "../../common/Ui/TextInput";
import Button from "../../common/Ui/Button";
import ApiService from "../../common/Api/ApiService";
import Modal from '../../common/Ui/Modal';  

function BusinessTypeRegisterForm(props){
  
  const [isModalOpen,setModalOpen]=useState(false);
  const [typename,setTypename]= useState('');
  const [description,setDescription]= useState('');
  const [sectorCode,setSectorCode]= useState('');
  const [typeCode,setTypeCode]= useState('');

  const handleTypeSubmit = async(e) => {
    e.preventDefault();
    const typedto={
      id: null,
      typeName: typename,
      sectorCode: sectorCode,
      typeCode: typeCode,
      description: description,
      petBusinessDTOList: []
    }
    const response = await ApiService.businessTypeService.register(typedto);
    const result=response.data;
    if(result.result){
      const newtype=result.type;

      alert(result.message
        + '\n 새롭게 추가된 타입 정보'
        + '\n 타입 아이디: '+ newtype.id
        + '\n 타입 이름: '+ newtype.typeName
        + '\n 섹터 코드: '+ newtype.sectorCode
        + '\n 타입 코드: '+ newtype.typeCode
        + '\n 타입 설명: '+ newtype.description);
      setModalOpen(false);

    }else{
      alert(result.message);
    }
  }
  
  return (
    <div className="container py-4">
      <form onSubmit={handleTypeSubmit} className="border p-4 rounded">
        <h5>타입 추가 창</h5>
        <p className="text-muted mb-4">별도의 타입을 추가해보세요!</p>

        <div className="mb-3">
          <CustomLabel classtext="" title="타입 이름" for="typeName" />
          <TextInput
            classtext="form-control"
            id="typeName"
            name="typeName"
            value={typename}
            placeholderText="새로운 사업자 타입 이름을 입력하세요"
            onChange={e => setTypename(e.target.value)}
          />
        </div>

        <div className="mb-3">
          <CustomLabel classtext="" title="섹터 코드" for="sectorCode" />
          <TextInput
            classtext="form-control"
            id="sectorCode"
            name="sectorCode"
            value={sectorCode}
            placeholderText="새로운 섹터 코드를 입력하세요"
            onChange={e => setSectorCode(e.target.value)}
          />
        </div>

        <div className="mb-3">
          <CustomLabel classtext="" title="타입 코드" for="typeCode" />
          <TextInput
            classtext="form-control"
            id="typeCode"
            name="typeCode"
            value={typeCode}
            placeholderText="새로운 타입 코드를 입력하세요"
            onChange={e => setTypeCode(e.target.value)}
          />
        </div>

        <div className="mb-4">
          <CustomLabel classtext="" title="타입 설명" for="description" />
          <textarea
            id="description"
            name="description"
            className="form-control"
            rows={3}
            placeholder="사업자 타입에 대한 설명을 작성해 주세요."
            value={description}
            onChange={e => setDescription(e.target.value)}
          />
        </div>

        <Button
          classtext="btn btn-primary me-2"
          type="button"
          title="사업 타입 추가"
          onClick={()=>setModalOpen(true)}
        />

        <Modal isOpen={isModalOpen} onClose={() => setModalOpen(false)}>
          <CusomP classtext="mb-3" title="정말로 추가 하겠습니까?" />
          <div className="mt-3 text-end">
            <Button
              classtext="btn btn-danger me-2"
              type="button"
              title="아니요"
              onClick={() => setModalOpen(false)}
            />
            <Button
              classtext="btn btn-success"
              type="submit"
              title="예"
              onClick={handleTypeSubmit}
            />
          </div>
        </Modal>
      </form>
    </div>
  );
}

export default BusinessTypeRegisterForm;
