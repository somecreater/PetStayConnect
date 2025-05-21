import React from "react";

function Account(props){
  const {account}=props;
  
  return (
    <div className="container py-4">
      <div className="card-header">{`#${account.id} 정산 계좌 정보(서비스용)`}</div>
      <div className="card-body">
        <dl className="row g-2 mb-4">

          <dt className="col-sm-3">계좌 타입</dt>
          <dd className="col-sm-9">
            <p className="mb-0">{account.accountType}</p>
          </dd>

          <dt className="col-sm-3">총 거래 합산 금액</dt>
          <dd className="col-sm-9">
            <p className="mb-0">{account.amount}</p>
          </dd>

          <dt className="col-sm-3">계좌 소유주</dt>
          <dd className="col-sm-9">
            <p className="mb-0">{account.userLoginId}</p>
          </dd>
        </dl>
      </div>    
    </div>
  );
}

export default Account;