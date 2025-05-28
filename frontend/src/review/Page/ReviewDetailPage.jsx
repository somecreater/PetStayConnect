import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useUser } from '../../common/Context/UserContext';
import ApiService from '../../common/Api/ApiService';
import ReviewUpdateForm from '../Form/ReviewUpdateForm';
import ReviewDeleteButton from '../Component/ReviewDeleteButton';
import StarRating from '../Component/StarRating';
import Button from '../../common/Ui/Button';

export default function ReviewDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useUser();

  const [review, setReview] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isEditing, setIsEditing] = useState(false);

  // 상세 데이터 불러오기
  useEffect(() => {
    ApiService.reviews
      .detail(id)
      .then(({ data }) => setReview(data))
      .catch(() => setError('리뷰 정보를 불러오는 중 오류가 발생했습니다.'))
      .finally(() => setLoading(false));
  }, [id]);

  const handleDelete = () => {
    if (!window.confirm('정말 이 리뷰를 삭제하시겠습니까?')) return;

    ApiService.reviews
      .delete(id)
      .then(() => {
        // 삭제 성공하면 리스트 페이지로 이동
        navigate('/reviews');
        window.location.reload();
      })
      .catch(err => {
        console.error('삭제 실패', err);
        alert('삭제 중 오류가 발생했습니다.');
      });
  };

  if (loading) {
    return <div className="container py-5 text-center">로딩 중...</div>;
  }
  if (error) {
    return (
      <div className="container py-5">
        <div className="alert alert-danger text-center">{error}</div>
      </div>
    );
  }
  if (!review) {
    return <div className="container py-5 text-center">해당 리뷰를 찾을 수 없습니다.</div>;
  }
    const isAuthor = String(user?.id) === String(review.userId) ||
                       (user?.userLoginId && user.userLoginId === review.userLoginId);

    if (isEditing && isAuthor) {
    return (
      <div className="container mt-4">
        <div className="card">
          <div className="card-body" style={{ minHeight: '500px' }}>

            <ReviewUpdateForm
              review={review}
              onSuccess={() => {
                setIsEditing(false);
                // 수정 후 다시 상세 불러오기
                ApiService.reviews.detail(id).then(({ data }) => setReview(data));
              }}
              onCancel={() => setIsEditing(false)}
            />
          </div>
        </div>
      </div>
    );
  }

  const {
    id: reviewId,
    petBusinessName,
    petBusinessLocation,
    rating,
    content,
    reservationId,
    createdAt,
  } = review;
  const formattedDate = new Date(createdAt).toLocaleDateString('ko-KR');

  return (
    <div className="container mt-4">
      <div className="card shadow-sm">
        {/* Header */}
        <div className="card-header border-bottom-0">
          <div className="d-flex justify-content-between align-items-start">
            <div>
              <h5 className="mb-1">{petBusinessName || '호텔명 없음'}</h5>
              <p className="text-muted mb-0">{petBusinessLocation || '위치 정보 없음'}</p>
            </div>
            <div className="text-muted text-end" style={{ fontSize: '0.9rem' }}>
              <div>리뷰 #{reviewId}</div>
              <div>{formattedDate}</div>
            </div>
          </div>
        </div>

        {/* Body */}
        <div className="card-body">
          <div className="d-flex align-items-center mb-3">
            <StarRating rating={rating} readOnly />
            <span className="ms-2 fw-semibold">{rating} / 5</span>
          </div>
          <p className="fs-4 mb-3">{content || '작성된 리뷰가 없습니다.'}</p>
          <p className="fw-medium">예약번호: {reservationId}</p>
        </div>

        {/* Footer */}
        {isAuthor && (
        <div className="card-footer bg-white border-top-0 d-flex justify-content-end gap-2">
          <Button
            classtext="btn btn-primary btn-sm"
            title="수정"
            onClick={() => setIsEditing(true)}
          />
          <ReviewDeleteButton
              reviewId={id}
              onClick={handleDelete}
              onDeleted={() => navigate('/reviews')}
            />
        </div>
        )}
      </div>
    </div>
  );
}
