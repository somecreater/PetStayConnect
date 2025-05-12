// src/review/Form/ReviewRegisterForm.jsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ApiService from '../../common/Api/ApiService';
import TextInput from '../../common/Ui/TextInput';
import Button from '../../common/Ui/Button';

export default function ReviewRegisterForm() {
  const [form, setForm] = useState({ reservationId: '', rating: '', content: '' });
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async e => {
    e.preventDefault();
    setError(null);
    try {
      await ApiService.reviews.register({
        reservationId: Number(form.reservationId),
        rating: Number(form.rating),
        content: form.content,
      });
      navigate('/reviews');
    } catch (err) {
      setError(err.response?.data?.message || '리뷰 등록 중 오류가 발생했습니다.');
    }
  };

  return (
    <div className="container py-4">
      <form onSubmit={handleSubmit} className="card p-4">
        <h5 className="card-title mb-3">리뷰 등록</h5>

        {error && <div className="alert alert-danger">{error}</div>}

        <div className="mb-3">
          <label htmlFor="reservationId" className="form-label">예약 ID</label>
          <input
            type="number"
            id="reservationId"
            name="reservationId"
            className="form-control"
            value={form.reservationId}
            onChange={handleChange}
            placeholder="예약 ID를 입력하세요"
          />
        </div>

        <div className="mb-3">
          <label htmlFor="rating" className="form-label">평점</label>
          <input
            type="number"
            id="rating"
            name="rating"
            className="form-control"
            value={form.rating}
            onChange={handleChange}
            placeholder="평점을 입력하세요 (1-5)"
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
            placeholder="리뷰를 입력하세요"
          />
        </div>

        <div className="d-grid">
          <Button type="submit" title="등록" classtext="btn btn-primary" />
        </div>
      </form>
    </div>
  );
}
