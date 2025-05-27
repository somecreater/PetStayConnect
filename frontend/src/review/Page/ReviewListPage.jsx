import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useLocation } from 'react-router-dom';
import ApiService from '../../common/Api/ApiService';
import Button from '../../common/Ui/Button';
import ReviewItem from '../Component/ReviewItem';
import Pagination from '../../common/Ui/Pagination';

export default function ReviewListPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const [reviews, setReviews] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages]   = useState(1);
  const size = 10;
  const [loading, setLoading] = useState(true);
  const [fetchError, setFetchError] = useState(null);

//   // 1) 목록을 가져오는 함수로 분리
//   const loadReviews = () => {
//     setLoading(true);
//     ApiService.reviews
//       .list({ page: 0, size: 50 })
//       .then(res => {
//         const list = Array.isArray(res.data) ? res.data : res.data.reviews;
//         setReviews(list || []);
//       })
//       .catch(() => setFetchError('리뷰 목록을 불러오는 데 실패했습니다.'))
//       .finally(() => setLoading(false));
//   };


    const loadReviews = page => {
       setLoading(true);
       ApiService.reviews
         .list(page, size)
         .then(res => {
           setReviews(res.data.content);
           setCurrentPage(res.data.number);
           setTotalPages(res.data.totalPages);
         })
         .catch(() => setFetchError('리뷰 목록을 불러오는 데 실패했습니다.'))
         .finally(() => setLoading(false));
    };

  // 2) 빈 deps 배열로 컴포넌트 마운트 시, 그리고 다시 돌아왔을 때마다 호출
  useEffect(() => {
    loadReviews(0);
  }, [location]);


  const deleteReview = id => {
      if (!window.confirm('정말 이 리뷰를 삭제하시겠습니까?')) return;
      ApiService.reviews
        .delete(id)
        .then(() => {
          loadReviews();  // ← 여기서 목록을 다시 불러옵니다
        })
        .catch(err => {
          console.error('삭제 실패', err);
          alert('삭제 중 오류가 발생했습니다.');
        });
  };

  if (loading) return <p className="container py-4">로딩 중…</p>;
  if (fetchError) return <div className="container py-4"><div className="alert alert-danger">{fetchError}</div></div>;

  return (
    <div className="container py-5">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1 className="h3 mb-0">리뷰 목록</h1>
        <Button
          title="+ 새 리뷰 작성"
          onClick={() => navigate('/reviews/register')}
          classtext="btn btn-primary"
        />
      </div>

      {reviews.length > 0 ? (
        reviews.map(r => (
          <ReviewItem key={r.id} review={r} />
        ))
      ) : (
        <div className="alert alert-info">등록된 리뷰가 없습니다.</div>
      )}

      <Pagination
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={loadReviews}
      />
    </div>
  );

}
