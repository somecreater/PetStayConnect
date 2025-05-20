import React, { useState } from "react";
import Pay from "../Component/Pay";

function PayList({List ,isDelete, onDelete, isBusiness, onSelected}){

  const handleClick= (pay)=>{
    onSelected(pay);
  }

  if(!List || List.length === 0){
    return <div className="text-center text-muted">결제 내역이 없습니다.</div>
  }
  return (
    <>
    <h5 className="mb-3">
      {isBusiness ? "결제 내역(사업자)" : "결제 내역"}
    </h5>
    <div className="row gy-4">
      
      {List.map(pay => (
        <div key={pay.id} className="col-12" onClick={() =>handleClick(pay)}>
          <Pay PayDTO={pay}/>
          {isDelete && pay.paymentStatus!=='CANCELED' && (
            <button
                classtext="btn btn-outline-danger btn-sm"
                type="button"
                onClick={() =>{
                  e.stopPropagation();
                  onDelete(pay);
                }}
            >
              환불 처리
            </button>
          )}
          
        </div>
      ))}
    </div>
    </>
  );
}

export default PayList;