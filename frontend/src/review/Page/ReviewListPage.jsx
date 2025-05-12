import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import ApiService from '../../common/Api/ApiService';
import Button from '../../common/Ui/Button';

export default function ReviewListPage() {
    console.log('ApiService ▶', ApiService);

  const [reviews, setReviews] = useState([]);
  const navigate = useNavigate();

  const loadReviews = async () => {
    try {
      const res = await ApiService.reviews.list();
     console.log('▶ review API res.data:', res.data);

      setReviews(res.data);
    } catch (error) {
      console.error('Failed to load reviews', error);
    }
  };

  useEffect(() => {
    loadReviews();
  }, []);

  return (
    <div className="container py-4">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h2 className="mb-0">나의 리뷰</h2>
        <Button
          type="button"
          title="리뷰 작성"
          classtext="btn btn-primary"
          onClick={() => navigate('/reviews/register')}
        />
      </div>

      {reviews.length > 0 ? (
        <ul className="list-group">
          {reviews.map(r => (
            <li key={r.id} className="list-group-item d-flex justify-content-between align-items-center">
              <Link to={`/reviews/${r.id}`} className="text-decoration-none">
                {r.petBusinessName} – 평점: {r.rating}
              </Link>
              <span className="text-muted">{new Date(r.createdAt).toLocaleDateString()}</span>
            </li>
          ))}
        </ul>
      ) : (
        <div className="alert alert-info">등록된 리뷰가 없습니다.</div>
      )}
    </div>
  );
}
