import React from 'react';
import { useNavigate } from 'react-router-dom';
import PostRegisterForm from '../form/PostRegisterForm';

export default function PostRegisterPage() {
  const navigate = useNavigate();

  return (
    <div className="container py-4">
      <h1 className="mb-4">질문 등록</h1>
      <div className="card">
        <div className="card-body">
          <PostRegisterForm onSuccess={() => navigate('/qnas')} />
        </div>
      </div>
    </div>
  );
}
