import React from "react";
import BusinessTypeList from "../Component/BusinessTypeList";
import CusomP from "../../common/Ui/CusomP";

function BusinessTypePage(props){


  return (
    <div className="container py-4">
      <div className="mb-3">
        <CusomP classtext="h4 mb-0" title="사업자 타입 리스트" />
      </div>
      <BusinessTypeList />
    </div>
  );
}

export default BusinessTypePage;
