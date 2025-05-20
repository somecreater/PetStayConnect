import React, { useState } from "react";
import dayjs from 'dayjs';

function formatDate(value, format = 'YYYY-MM-DD') {
  return value && dayjs(value).isValid()
    ? dayjs(value).format(format)
    : '—';
}

function formatPercent(value) {
  return typeof value === 'number'
    ? `${(value * 100).toFixed(2)}%`
    : '—';
}

function Pay({PayDTO}){

  const [expanded, setExpanded] = useState(false);
  const toggle = () => setExpanded(prev => !prev);

  const {
    amount,
    feeRate,
    id,
    impUid,
    merchantUid,
    paymentMethod,
    paymentStatus,
    refundAmount,
    refundStatus,
    reservationId,
    serviceFee,
    transactionId,
    transactionTime,
  } = PayDTO;

  const items=[
    ['결제 ID', id],
    ['예약 ID', reservationId],
    ['아임포트 UID', impUid],
    ['상점 주문번호', merchantUid],
    ['결제 금액', amount != null ? `${amount}원` : '—'],
    ['수수료율', formatPercent(feeRate)],
    ['서비스 수수료', serviceFee != null ? `${serviceFee}원` : '—'],
    ['결제 상태', paymentStatus || '—'],
    ['결제 수단', paymentMethod || '—'],
    ['거래 ID', transactionId || '—'],
    ['거래 시간', formatDate(transactionTime, 'YYYY-MM-DD HH:mm')],
    ['환불 상태', refundStatus || '—'],
    ['환불 금액', refundAmount != null ? `${refundAmount}원` : '—'],
  ];
  return (
    <div  className="card mb-3">
      <div
        className="card-header d-flex justify-content-between align-items-center"
        style={{ cursor: 'pointer' }}
        onClick={toggle}
      >
        <strong>{`${id}# 결제 정보`}</strong>
        <span>{expanded ? '▲' : '▼'}</span>
      </div>

      {expanded && (
        <ul className="list-group list-group-flush">
          {items.map(([label, value]) => (
            <li
              key={label}
              className="list-group-item d-flex justify-content-between"
            >
              <p className="mb-0"><strong>{label}</strong></p>
              <p className="mb-0">{value}</p>
            </li>
          ))}
        </ul>
      )}

    </div>
  );
}

export default Pay;