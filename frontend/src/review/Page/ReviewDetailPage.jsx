// src/review/Page/ReviewDetailPage.jsx
import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import ApiService from '../../common/Api/ApiService';
import Button from '../../common/Ui/Button';
import ReviewDeleteButton from '../Component/ReviewDeleteButton';
import ReviewUpdateForm from '../Form/ReviewUpdateForm';

export default function ReviewDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [review, setReview] = useState(null);
  const [isEditing, setIsEditing] = useState(false);

  const loadReview = async () => {
    try {
      const res = await ApiService.reviews.detail(id);
      setReview(res.data);
    } catch (error) {
      console.error('Failed to load review', error);
    }
  };

  useEffect(() => {
    loadReview();
  }, []);

  if (!review) return <div>Loading...</div>;

  return (
    <div className="container py-4">
      <div className="card mb-3">
        <div className="card-header d-flex justify-content-between align-items-center">
          <h5 className="mb-0">리뷰 #{review.id}</h5>
          {!isEditing && (
            <div className="btn-group">
              <Button type="button" title="수정" classtext="btn btn-outline-secondary" onClick={() => setIsEditing(true)} />
              <ReviewDeleteButton reviewId={review.id} onDeleted={() => navigate('/reviews')} />
            </div>
          )}
        </div>

        <div className="card-body">
          {isEditing ? (
            <ReviewUpdateForm
              review={review}
              onSuccess={() => { setIsEditing(false); }}
              onCancel={() => setIsEditing(false)}
            />
          ) : (
            <>
              <p><strong>평점:</strong> {review.rating}</p>
              <p><strong>내용:</strong> {review.content}</p>
              <p><strong>업체명:</strong> {review.petBusinessName}</p>
              <p><strong>위치:</strong> {review.petBusinessLocation}</p>
              <p className="text-muted"><strong>작성일:</strong> {new Date(review.createdAt).toLocaleString()}</p>
            </>
          )}
        </div>
      </div>

      <Link to="/reviews" className="btn btn-link">목록으로 돌아가기</Link>
    </div>
  );
}
