import React from 'react';
import { useNavigate } from 'react-router-dom';
import PostRegisterForm from '../form/PostRegisterForm';

export default function PostRegisterPage() {
  const navigate = useNavigate();

  return (
    <main className="PostRegisterPage">
      <h1 className="PageTitle">질문 등록</h1>
      <section className="FormSection">
        <PostRegisterForm
          onSuccess={() => navigate('/qnas')}
        />
      </section>
    </main>
  );
}
