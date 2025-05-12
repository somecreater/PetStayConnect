// src/review/Page/ReviewRegisterPage.jsx
import React from 'react';
import ReviewRegisterForm from '../Form/ReviewRegisterForm';

export default function ReviewRegisterPage() {
  return (
    <div className="container py-4">
      <h2 className="mb-4">리뷰 작성</h2>
      <ReviewRegisterForm />
    </div>
  );
}
