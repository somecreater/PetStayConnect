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
  
  return  (
    <div className="container py-4">
      <form onSubmit={handleTypeSubmit} className="border p-4 rounded bg-light">
        <h5 className="mb-3">타입 수정 창</h5>
        <p className="text-muted mb-4">섹터 코드와 타입 코드는 수정이 불가능합니다.</p>

        <div className="mb-3 row">
          <label className="col-sm-3 col-form-label">타입 아이디</label>
          <div className="col-sm-9">
            <CusomP classtext="form-control-plaintext" title={exId} />
          </div>
        </div>

        <div className="mb-3 row">
          <label htmlFor="typeName" className="col-sm-3 col-form-label">타입 이름</label>
          <div className="col-sm-9">
            <TextInput
              classtext="form-control"
              id="typeName"
              name="typeName"
              value={typename}
              placeholderText="새로운 타입 이름"
              onChange={e => setTypename(e.target.value)}
            />
          </div>
        </div>

        <div className="mb-3 row">
          <label className="col-sm-3 col-form-label">섹터 코드</label>
          <div className="col-sm-9">
            <CusomP classtext="form-control-plaintext" title={exSectorCode} />
          </div>
        </div>

        <div className="mb-3 row">
          <label className="col-sm-3 col-form-label">타입 코드</label>
          <div className="col-sm-9">
            <CusomP classtext="form-control-plaintext" title={exTypeCode} />
          </div>
        </div>

        <div className="mb-4">
          <label htmlFor="description" className="form-label">타입 설명</label>
          <textarea
            id="description"
            className="form-control"
            rows={3}
            value={description}
            onChange={e => setDescription(e.target.value)}
          />
        </div>

        <div className="d-flex gap-2">
          <Button
            classtext="btn btn-primary"
            type="button"
            title="변경 적용"
            onClick={()=>setModalOpen(true)}
          />
        </div>

        <Modal isOpen={isModalOpen} onClose={() => setModalOpen(false)}>
          <CusomP classtext="mb-3" title="정말로 수정 하시겠습니까?" />
          <div className="text-end">
            <Button
              classtext="btn btn-secondary me-2"
              type="button"
              title="아니요"
              onClick={() => setModalOpen(false)}
            />
            <Button
              classtext="btn btn-danger"
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

export default BusinessTypeUpdateForm;
