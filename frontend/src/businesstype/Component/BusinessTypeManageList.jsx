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
    <div className="container py-4">
      {types && types.length > 0 ? (
        <div
          className="row g-4 overflow-auto"
          style={{ maxHeight: '70vh' }}
        >
          {types.map(type => (
            <div key={type.id} className="col-md-6 col-lg-4">
              <div className="card h-100">
                <div className="card-body d-flex flex-column">
                  <BusinessType 
                    id={type.id} 
                    typeName={type.typeName}
                    sectorCode={type.sectorCode}
                    typeCode={type.typeCode}
                    description={type.description}
                  />

                  <div className="mt-auto d-flex gap-2">
                    <BusinessTypeDeleteButton 
                      type_id={type.id} 
                      businesstype={type}
                    />
                    <Button 
                      classtext="btn btn-outline-secondary"
                      type="button"
                      title="타입 수정"
                      onClick={() => openEditModal(type)}
                    />
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <div className="alert alert-warning text-center">
          <CusomP classtext="mb-0" title="타입이 존재하지 않습니다." />
        </div>
      )}

      {selectedType && (
        <Modal isOpen={isModalOpen} onClose={closeModal}>
          <BusinessTypeUpdateForm 
            exId={selectedType.id}
            exTypename={selectedType.typeName}
            exSectorCode={selectedType.sectorCode}
            exTypeCode={selectedType.typeCode}
            exDescription={selectedType.description}
            onSuccess={() => {
              onRefresh();
              closeModal();
            }}
          />
        </Modal>
      )}
    </div>
  );
}

export default BusinessManageTypeList;
