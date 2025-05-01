import React, { useEffect, useState } from "react";
import ApiService from "../../common/Api/ApiService";
import BusinessType from "./BusinessType";
import CusomP from "../../common/Ui/CusomP";
import Modal from "../../common/Ui/Modal";
import Button from "../../common/Ui/Button";
import BusinessTypeUpdateForm from "../Form/BusinessTypeUpdateForm";
import BusinessTypeDeleteButton from "../Component/BusinessTypeDeleteButton";

function BusinessManageTypeList(props){
  const {types, onRefresh}= props;
  const [isModalOpen,setIsModalOpen]=useState(false);
  const [selectedType, setSelectedType] = useState(null);


  const openEditModal = (type) => {
    setSelectedType(type);
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setSelectedType(null);
  };

  return(
    <>
      <div>
      {
        types && types.length>0 ?  
        <div className="BusinessTypeManageList">
        {types.map((type) => 
        <>
          <div className="BusinessTypeManage">
            <BusinessType 
              key={type.id} 
              id={type.id} 
              typeName={type.typeName}
              sectorCode={type.sectorCode}
              typeCode={type.typeCode}
              description={type.description}
            />
            <div className="buttonset">
                <BusinessTypeDeleteButton type_id={type.id} businesstype={type}/>
                <Button 
                  classtext={'BusinessTypeButton'} 
                  type="button" 
                  title={'타입 수정'} 
                  onClick={() => openEditModal(type)}
                />
              
            </div>
          </div>
        </>
        )}
        </div>
        :<div className="BusinessTypeList">
            <CusomP classtext={'noting'} title={'타입이 존재하지 않습니다.'}/>
        </div>
      }
      {selectedType && (
        <Modal isOpen={isModalOpen} onClose={closeModal}>
          <BusinessTypeUpdateForm 
            exId={selectedType.id}
            exTypename={selectedType.typeName}
            exSectorCode={selectedType.sectorCode}
            exTypeCode={selectedType.typeCode}
            exDescription={selectedType.description}
            onSuccess={()=>{
              onRefresh();
              closeModal();
            }}
          />
        </Modal>
      )}
      </div>
    </>
  );
}

export default BusinessManageTypeList;