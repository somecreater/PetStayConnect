// src/review/Form/ReviewUpdateForm.jsx
import React, { useState } from 'react';
import ApiService from '../../common/Api/ApiService';
import TextInput from '../../common/Ui/TextInput';
import Button from '../../common/Ui/Button';

export default function ReviewUpdateForm({ review, onSuccess, onCancel }) {
  const [form, setForm] = useState({
    rating: review.rating,
    content: review.content,
  });
  const [error, setError] = useState(null);

  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async e => {
    e.preventDefault();
    setError(null);
    try {
      await ApiService.reviews.update(review.id, {
        id: review.id,
        rating: Number(form.rating),
        content: form.content,
      });
      onSuccess();
    } catch (err) {
      setError(err.response?.data?.message || '리뷰 수정 중 오류가 발생했습니다.');
    }
  };

  return (
    <div className="container py-4">
      <form onSubmit={handleSubmit} className="card p-4">
        <h5 className="card-title mb-3">리뷰 수정</h5>

        {error && <div className="alert alert-danger">{error}</div>}

        <div className="mb-3">
          <label htmlFor="rating" className="form-label">평점</label>
          <input
            type="number"
            id="rating"
            name="rating"
            className="form-control"
            value={form.rating}
            onChange={handleChange}
            min="1"
            max="5"
          />
        </div>

        <div className="mb-3">
          <label htmlFor="content" className="form-label">리뷰 내용</label>
          <textarea
            id="content"
            name="content"
            className="form-control"
            value={form.content}
            onChange={handleChange}
            rows="4"
          />
        </div>

        <div className="d-flex gap-2">
          <Button type="submit" title="저장" classtext="btn btn-success" />
          <Button type="button" title="취소" onClick={onCancel} classtext="btn btn-secondary" />
        </div>
      </form>
    </div>
  );
}
