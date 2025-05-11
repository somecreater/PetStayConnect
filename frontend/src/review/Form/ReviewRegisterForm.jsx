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
    <form onSubmit={handleSubmit} className="ReviewRegisterForm">
      {error && <div>{error}</div>}
      <TextInput
        name="reservationId"
        value={form.reservationId}
        onChange={handleChange}
        placeholderText="예약 ID"
      />
      <TextInput
        name="rating"
        value={form.rating}
        onChange={handleChange}
        placeholderText="평점"
      />
      <TextInput
        name="content"
        value={form.content}
        onChange={handleChange}
        placeholderText="리뷰 내용"
      />
      <Button type="submit" title="등록" />
    </form>
  );
}
