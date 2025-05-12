import React from 'react';
import { useNavigate } from 'react-router-dom';
import RegisterForm from '../Form/RegisterForm';
import '../../common/Css/common.css';

function RegisterPage(props){
  
  const navigate = useNavigate();

  return (
    <div className="container py-5" style={{ maxWidth: 500 }}>
      <RegisterForm />
    </div>
  );
}

export default RegisterPage;
