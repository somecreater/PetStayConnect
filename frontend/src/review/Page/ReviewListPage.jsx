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
    <div className="ReviewListPage">
      <h2>나의 리뷰</h2>
      <Button
        type="button"
        title="리뷰 작성"
        onClick={() => navigate('/reviews/register')}
      />
      <ul>
        {reviews.length > 0 ? (
          reviews.map(r => (
            <li key={r.id}>
              <Link to={`/reviews/${r.id}`}>
                {r.petBusinessName} – 평점: {r.rating}
              </Link>
            </li>
          ))
        ) : (
          <p>등록된 리뷰가 없습니다.</p>
        )}
      </ul>
    </div>
  );
}
