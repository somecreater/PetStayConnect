import React, { useState } from "react";
import dayjs from 'dayjs';
import { FaChevronDown, FaChevronUp } from 'react-icons/fa';

function formatDate(value, format = 'YYYY-MM-DD') {
  return value && dayjs(value).isValid()
    ? dayjs(value).format(format)
    : '—';
}

function getStatusBadge(status) {
  switch (status) {
    case 'CONFIRMED': return 'badge bg-success';
    case 'CANCELLED': return 'badge bg-danger';
    case 'COMPLETED': return 'badge bg-primary';
    default: return 'badge bg-secondary';
  }
}

export default function Reservation({ reservationDTO }) {
  const [expanded, setExpanded] = useState(false);
  const toggle = () => setExpanded(prev => !prev);

  const {
    id,
    userLoginId,
    petBusinessName,
    petBusinessRegisterNumber,
    checkIn,
    checkOut,
    period,
    specialRequests,
    businessRequestInfo,
    status,
    paymentDTO,
    reviewDTO,
    createdAt,
    updatedAt,
  } = reservationDTO;

  const items = [
    ['예약 번호', id],
    ['회원 아이디', userLoginId],
    ['업체 이름', petBusinessName],
    ['등록 번호', petBusinessRegisterNumber],
    ['체크인', formatDate(checkIn)],
    ['체크아웃', formatDate(checkOut)],
    ['숙박 기간', period ? `${period}박` : '—'],
    ['특별 요청', specialRequests || '—'],
    ['사업자 요청', businessRequestInfo || '—'],
    ['결제 금액', paymentDTO ? `${paymentDTO.amount.toLocaleString()}원` : '—'],
    ['결제 수단', paymentDTO ? paymentDTO.paymentMethod : '—'],
    ['리뷰', reviewDTO ? reviewDTO.content : '작성 전'],
    ['생성일', formatDate(createdAt, 'YYYY-MM-DD HH:mm')],
    ['수정일', formatDate(updatedAt, 'YYYY-MM-DD HH:mm')],
  ];

  return (
    <div className="card mb-4 shadow-sm">
      <div
        className="card-header d-flex justify-content-between align-items-center"
        style={{ cursor: 'pointer' }}
        onClick={toggle}
      >
        <div>
          <strong className="me-2">#{id}</strong>
          <span className={getStatusBadge(status)}>{status}</span>
        </div>
        {expanded ? <FaChevronUp /> : <FaChevronDown />}
      </div>

      {expanded && (
        <div className="card-body p-0">
          <table className="table table-borderless mb-0">
            <tbody>
              {items.map(([label, value]) => (
                <tr key={label}>
                  <th className="px-3 py-2 text-end" style={{ width: '40%' }}>
                    {label}
                  </th>
                  <td className="px-3 py-2">
                    {value}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
