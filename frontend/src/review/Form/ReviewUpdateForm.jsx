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
    <form onSubmit={handleSubmit} className="ReviewUpdateForm">
      {error && <div>{error}</div>}
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
      <Button type="submit" title="저장" />
      <Button type="button" title="취소" onClick={onCancel} />
    </form>
  );
}
