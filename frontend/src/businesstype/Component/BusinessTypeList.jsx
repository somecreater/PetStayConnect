import React, { useEffect, useState } from "react";
import ApiService from "../../common/Api/ApiService";
import BusinessType from "./BusinessType";
import CusomP from "../../common/Ui/CusomP";

function BusinessTypeList(props){
  const [typelist,setTypelist]= useState([]);

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

  if (!typelist || typelist.length === 0) {
    return (
      <div className="container py-4">
        <div className="alert alert-warning">
          <CusomP classtext="text-center" title="타입이 존재하지 않습니다." />
        </div>
      </div>
    );
  }
  return(
    <div className="container py-4">
      <div
        className="row g-4 overflow-auto"
        style={{ maxHeight: '70vh' }}
      >
        {typelist.map(type => (
          <div key={type.id} className="col-sm-6 col-lg-4">
            <BusinessType
              id={type.id}
              typeName={type.typeName}
              sectorCode={type.sectorCode}
              typeCode={type.typeCode}
              description={type.description}
            />
          </div>
        ))}
      </div>
    </div>
  );
}

export default BusinessTypeList;
