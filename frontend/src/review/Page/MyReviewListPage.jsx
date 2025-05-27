import React, { useState, useEffect } from 'react';
import ApiService from '../../common/Api/ApiService';
import ReviewItem from '../Component/ReviewItem';

export default function MyReviewListPage() {
  const [reviews, setReviews] = useState([]);

  useEffect(() => {
    ApiService.reviews.myList()
      .then(res => setReviews(res.data))
      .catch(() => alert('내 리뷰 불러오기 실패'));
  }, []);

  return (
    <div className="container py-4">
      <h2>내가 쓴 리뷰</h2>
      {reviews.length > 0
        ? reviews.map(r => <ReviewItem key={r.id} review={r} />)
        : <div className="alert alert-info">작성한 리뷰가 없습니다.</div>
      }
    </div>
  );
}
