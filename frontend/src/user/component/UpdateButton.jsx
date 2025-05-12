import React from 'react';
import { useNavigate } from 'react-router-dom';
import Button from '../../common/Ui/Button';
import '../../common/Css/common.css';

function UpdateButton(props){
  const navigate=useNavigate();
  return(
    <Button
      classtext="btn btn-outline-primary"
      type="button"
      onClick={() => navigate('/user/update')}
      title="회원정보 수정"
    />
  );
}

export default UpdateButton;
