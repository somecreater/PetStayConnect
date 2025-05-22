import React from 'react';
import { useNavigate } from 'react-router-dom';
import StarRating from './StarRating';
import Button from '../../common/Ui/Button';


export default function ReviewItem({ review,onDelete }) {
  const navigate = useNavigate();
  const {
    id,
    reservationId,
    userLoginId,
    rating,
    content,
    petBusinessName,
    petBusinessLocation,
    createdAt
  } = review;

  return (
    <div className="card mb-4 shadow-sm w-75 mx-auto">
         <div
             className="card-body"
             style={{ cursor: 'pointer' }}
             onClick={() => navigate(`/reviews/${id}`)}
         >
          <div className="d-flex justify-content-between align-items-center mb-3">
            <div>
                <strong className="me-2">예약번호:</strong>
                <span>{reservationId}</span>
            </div>

            <span className="text-muted">
                {createdAt ? new Date(createdAt).toLocaleDateString('ko-KR') : ''}
            </span>
          </div>
        <div className="d-flex justify-content-center mb-3">
          <StarRating rating={rating} readOnly />
        </div>
        <p className="card-text mb-3" style={{ whiteSpace: 'pre-wrap' }}>
          {content || '작성된 리뷰가 없습니다.'}
        </p>
        <ul className="list-inline text-muted mb-0">
          {reservationId != null && (
            <li className="list-inline-item">
              <span className="fw-bold">작성자:{userLoginId || '익명'}</span>
            </li>
          )}
          {petBusinessName && (
            <li className="list-inline-item">
              <strong>호텔:</strong> {petBusinessName}
            </li>
          )}
          {petBusinessLocation && (
            <li className="list-inline-item">
              <strong>위치:</strong> {petBusinessLocation}
            </li>
          )}
        </ul>
      </div>
    </div>
  );
}
