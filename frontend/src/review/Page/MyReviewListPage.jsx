import React, { useState, useEffect } from 'react';
import ApiService from '../../common/Api/ApiService';
import ReviewItem from '../Component/ReviewItem';
import Pagination from '../../common/Ui/Pagination';

export default function MyReviewListPage() {
  const [reviews, setReviews]       = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages]   = useState(1);
  const size = 10;

  const loadMyReviews = page => {
    ApiService.reviews.myList(page, size)
      .then(res => {
        setReviews(res.data.content);
        setCurrentPage(res.data.number);
        setTotalPages(res.data.totalPages);
      })
      .catch(() => alert('내 리뷰 불러오기 실패'));
  };

  useEffect(() => {
    loadMyReviews(0);
  }, []);

  return (
    <div className="container py-4">
      <h2>내가 쓴 리뷰</h2>
      {reviews.length > 0 ? (
        reviews.map(r => <ReviewItem key={r.id} review={r} />)
      ) : (
        <div className="alert alert-info">작성한 리뷰가 없습니다.</div>
      )}
      <Pagination
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={loadMyReviews}
      />
    </div>
  );
}

