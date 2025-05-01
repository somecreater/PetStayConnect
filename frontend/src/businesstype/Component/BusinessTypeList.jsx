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

  return(
    <>
      <div>
      {
        typelist !== null ?  
        <div className="BusinessTypeList">
        {typelist.map((type) => 
        <>
            <BusinessType 
              key={type.id} 
              id={type.id} 
              typeName={type.typeName}
              sectorCode={type.sectorCode}
              typeCode={type.typeCode}
              description={type.description}
            />
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

export default BusinessTypeList;
