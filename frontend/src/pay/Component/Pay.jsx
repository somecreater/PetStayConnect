import React, { useState } from "react";
import dayjs from 'dayjs';
import { FaChevronDown, FaChevronUp } from 'react-icons/fa';

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

function getStatusBadge(status) {
  switch (status) {
    case 'PAID': return 'badge bg-success';
    case 'CANCELED': return 'badge bg-danger';
    case 'REFUNDED': return 'badge bg-warning text-dark';
    default: return 'badge bg-secondary';
  }
}

export default function Pay({ PayDTO }) {
  const [expanded, setExpanded] = useState(false);
  const toggle = () => setExpanded(prev => !prev);

  const {
    id,
    reservationId,
    impUid,
    merchantUid,
    amount,
    feeRate,
    serviceFee,
    paymentStatus,
    paymentMethod,
    transactionId,
    transactionTime,
    refundStatus,
    refundAmount,
  } = PayDTO;

  const items = [
    ['결제 ID', id],
    ['예약 ID', reservationId],
    ['아임포트 UID', impUid],
    ['상점 주문번호', merchantUid],
    ['결제 금액', amount != null ? amount.toLocaleString() + '원' : '—'],
    ['수수료율', formatPercent(feeRate)],
    ['서비스 수수료', serviceFee != null ? serviceFee.toLocaleString() + '원' : '—'],
    ['결제 수단', paymentMethod || '—'],
    ['거래 ID', transactionId || '—'],
    ['거래 시간', formatDate(transactionTime, 'YYYY-MM-DD HH:mm')],
    ['환불 상태', refundStatus || '—'],
    ['환불 금액', refundAmount != null ? refundAmount.toLocaleString() + '원' : '—'],
  ];

  return (
    <div className="card mb-4 shadow-sm">
      <div
        className="card-header d-flex justify-content-between align-items-center"
        style={{ cursor: 'pointer' }}
        onClick={toggle}
      >
        <div>
          <strong className="me-2">결제 #{id}</strong>
          <span className={getStatusBadge(paymentStatus)}>{paymentStatus}</span>
        </div>
        {expanded ? <FaChevronUp /> : <FaChevronDown />}
      </div>

      {expanded && (
        <div className="card-body p-0">
          <table className="table table-borderless mb-0">
            <tbody>
              {items.map(([label, value]) => (
                <tr key={label}>
                  <th className="px-3 py-2 text-end" style={{ width: '35%' }}>{label}</th>
                  <td className="px-3 py-2">{value}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
