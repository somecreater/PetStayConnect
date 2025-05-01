import React from 'react';
import { useNavigate }    from 'react-router-dom';

import PostRegisterForm from '../form/PostRegisterForm';

export default function PostRegisterPage() {
    const navigate = useNavigate();

      return (
        <div>
          <h1>질문 등록</h1>
          <PostRegisterForm
            onSuccess={() => navigate('/qnas')}
          />
        </div>
      );
    }