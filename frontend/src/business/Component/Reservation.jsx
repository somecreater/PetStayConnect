import React from "react";
import dayjs from 'dayjs';

function formatDate(value, format = 'YYYY-MM-DD') {
  return value && dayjs(value).isValid() ? dayjs(value).format(format) : '—';
}

function Reservation({ reservationDTO }){
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
    PetReservationDTOList,
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
    ['예약 상태', status],
    ['특별 요청', specialRequests || '—'],
    ['사업자 요청', businessRequestInfo || '—'],
    ['결제 정보', paymentDTO ? `${paymentDTO.amount}원 (${paymentDTO.method})` : '—'],
    ['리뷰', reviewDTO ? reviewDTO.content : '작성 전'],
    ['생성일', formatDate(createdAt, 'YYYY-MM-DD HH:mm')],
    ['수정일', formatDate(updatedAt, 'YYYY-MM-DD HH:mm')],
  ];

  return (
    <div className="card mb-3">
      <div className="card-header">
        <strong>예약 정보</strong>
      </div>
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
    </div>
  );
}

export default Reservation;