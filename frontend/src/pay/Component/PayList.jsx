import React from "react";
import Pay from "./Pay";

function PayList({List ,isDelete, onDelete, isBusiness}){
  if(!List || List.length === 0){
    return <div className="text-center text-muted">결제 내역이 없습니다.</div>
  
}
  return (
    <div className="row gy-4">
      {isBusiness === true ? <h5>결제 내역(사업자)</h5>:
      <h5>결제 내역</h5>
      }
      {List.map(pay => (
        <div key={pay.id} className="col-12">
          <Pay PayDTO={pay}/>
          {isDelete && (
            <button
                classtext="btn btn-outline-danger btn-sm"
                type="button"
                onClick={() => onDelete(pay.id)}
            >
              환불 처리
            </button>
          )}
          
        </div>
      ))}
    </div>
  );
}

export default PayList;