import React from 'react';
import { useNavigate } from 'react-router-dom';
import LoginForm from '../Form/LoginForm';
import '../../common/Css/common.css';

function LoginPage(props){
  
  const navigate = useNavigate();
  

  return (
    <div className="container py-5" style={{ maxWidth: 400 }}>
      <LoginForm />
    </div>
  );
}

export default LoginPage;
