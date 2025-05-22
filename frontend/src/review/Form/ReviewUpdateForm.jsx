import React, { useState } from 'react';
import StarRating from '../Component/StarRating';
import ApiService from '../../common/Api/ApiService';
import Button from '../../common/Ui/Button';

export default function ReviewUpdateForm({ review, onSuccess, onCancel }) {
  const [rating, setRating] = useState(review.rating);
  const [content, setContent] = useState(review.content);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await ApiService.reviews.update(review.id, {
        rating: rating,
        content: content,
        petBusinessName: review.petBusinessName,
        petBusinessLocation: review.petBusinessLocation,
        reservationId: review.reservationId,
        reportCount: review.reportCount,
        userId: review.userId,
      });
      onSuccess();
    } catch {
      alert('수정에 실패했습니다.');
    }
  };

  return (
    <form onSubmit={handleSubmit} className="card p-4 bg-light">
      <h5 className="mb-3 text-center">리뷰 수정</h5>

      <div className="mb-3">
        <label className="form-label">평점</label>
        <div>
          <StarRating
            rating={rating}
            onChange={(newRating) => setRating(newRating)}
          />
        </div>
      </div>

      <div className="mb-3">
        <label className="form-label">리뷰 내용</label>
        <textarea
          className="form-control"
          rows={8}
          style={{ minHeight: '200px' }}
          value={content}
          onChange={(e) => setContent(e.target.value)}
        />
      </div>

      <div className="text-end">
        <Button
          type="submit"
          classtext="btn btn-success me-2"
          title="저장"
        />
        <Button
          type="button"
          classtext="btn btn-secondary"
          title="취소"
          onClick={onCancel}
        />
      </div>
    </form>
  );
}
