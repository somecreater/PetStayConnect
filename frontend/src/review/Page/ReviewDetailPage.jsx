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
    <main className="ReviewDetailPage">
      <h2>리뷰 #{review.id}</h2>
      <p>평점: {review.rating}</p>
      <p>내용: {review.content}</p>
      <p>업체명: {review.petBusinessName}</p>
      <p>위치: {review.petBusinessLocation}</p>
      <p>작성일: {new Date(review.createdAt).toLocaleString()}</p>

      {!isEditing ? (
        <>
          <Button type="button" title="수정" onClick={() => setIsEditing(true)} />
          <ReviewDeleteButton
            reviewId={review.id}
            onDeleted={() => navigate('/reviews')}
          />
        </>
      ) : (
        <ReviewUpdateForm
          review={review}
          onSuccess={() => { setIsEditing(false); loadReview(); }}
          onCancel={() => setIsEditing(false)}
        />
      )}

      <Link to="/reviews">목록으로 돌아가기</Link>
    </main>
  );
}
