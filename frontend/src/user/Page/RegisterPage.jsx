import React from 'react';
import { useNavigate } from 'react-router-dom';
import RegisterForm from '../Form/RegisterForm';
import '../../common/Css/common.css';

function RegisterPage(props){
  
  const navigate = useNavigate();

  return (
    <>
      <div>

        <RegisterForm/>

      </div>
    </>
  );
}

export default RegisterPage;