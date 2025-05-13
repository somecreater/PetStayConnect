import React from "react";
import dayjs from 'dayjs';

function formatDate(value, format = 'YYYY-MM-DD') {
  return value && dayjs(value).isValid() ? dayjs(value).format(format) : '—';
}

function Pay({PayDTO}){
  
  const {id, reservationId, amount, feeRate, serviceFee, 
        paymentStatus, paymentMethod, transactionId, transactionTime, 
        refundStatus, refundAmount} = PayDTO;

  const items=[
    ['예약 ID ', reservationId],
    ['금액 ', `${amount}원`],
    ['수수료율 ', `${feeRate}%`],
    ['서비스 수수료 ', `${serviceFee}원`],
    ['결제 상태 ', paymentStatus],
    ['결제 방법 ', paymentMethod],
    ['거래 ID ', transactionId],
    ['거래 시간', formatDate(transactionTime, 'YYYY-MM-DD HH:mm')],
    ['환불 상태 ', refundStatus],
    ['환불 금액 ', `${refundAmount}원`]
  ];
  return (
    <div  className="card mb-3">
      <div className="card-header">
        <strong>{`${id}# 결제 정보`}</strong>
      </div>
      <ul className="list-group list-group-flush">
        {items.map(([label, value]) => (
          <li
            key={label}
            className="list-group-item d-flex justify-content-between"
          >
            <CustomP className="mb-0"><strong>{label}</strong></CustomP>
            <CustomP className="mb-0">{value}</CustomP>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default Pay;