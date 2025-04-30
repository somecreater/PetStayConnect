import React, { useEffect, useState } from "react";
import ApiService from "../../common/Api/ApiService";
import BusinessType from "./BusinessType";
import CusomP from "../../common/Ui/CusomP";
import Modal from "../../common/Ui/Modal";
import Button from "../../common/Ui/Button";
import BusinessTypeUpdateForm from "../Form/BusinessTypeUpdateForm";

function BusinessManageTypeList(props){
  const [typelist,setTypelist]= useState([]);
  const [isModalOpen,setModalOpen]=useState(false);

  useEffect(()=>{
    const fetchTypes = async () => {
      const response = await ApiService.businessTypeService.list();
      if(response.data.result === true){
        const typelist=response.data.typeList;
        setTypelist(typelist);
      }else{
        alert(response.data.message);
      }
    }
    fetchTypes();
  },[]);

  return(
    <>
      <div>
      {
        typelist !== null ?  
        <div className="BusinessTypeManageList">
        {typelist.map((type) => 
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
                  onClick={setModalOpen(!isModalOpen)}
                />
                <Modal isOpen={isModalOpen} onClose={() => setModalOpen(false)}>
                  <BusinessTypeUpdateForm 
                    exId={type.id}
                    exTypename={type.typeName}
                    exSectorCode={type.sectorCode}
                    exTypeCode={type.typeCode}
                    exDescription={type.description}
                  />
                </Modal>
            </div>
          </div>
        </>
        )}
        </div>
        :<div className="BusinessTypeList">
            <CusomP classtext={'noting'} title={'타입이 존재하지 않습니다.'}/>
        </div>
      }
      </div>
    </>
  );
}

export default BusinessManageTypeList;