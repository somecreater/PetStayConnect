import React from "react";
import BusinessTypeList from "../Component/BusinessTypeList";
import CusomP from "../../common/Ui/CusomP";

function BusinessTypePage(props){


  return (
    <>
      <div className="BusinessTypePage">
        <CusomP classtext={'alertText'} title={'사업자 타입 리스트'}/>

        <BusinessTypeList/>
        
      </div>
    </>
  );
}

export default BusinessTypePage;