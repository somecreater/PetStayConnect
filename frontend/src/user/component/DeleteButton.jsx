import React from 'react';
import { useNavigate } from 'react-router-dom';
import Button from '../../common/Ui/Button';
import '../../common/Css/common.css';

function DeleteButton(props){
  const navigate=useNavigate();
  return (
    <Button
      classtext="btn btn-warning"
      type="button"
      onClick={() => navigate('/user/delete')}
      title="회원 탈퇴"
    />
  );
}

export default DeleteButton;
