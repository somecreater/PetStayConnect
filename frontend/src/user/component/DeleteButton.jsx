import React from 'react';
import { useNavigate } from 'react-router-dom';
import Button from '../../common/Ui/Button';
import '../../common/Css/common.css';

function DeleteButton(props){
  const navigate=useNavigate();
  return (
    <>
      <Button
       classtext={'UserDeleteButton'} 
       type={'button'} 
       onClick={(e)=>{navigate('/user/delete')}} 
       title={'회원 탈퇴'}
      />
    </>
  );
}

export default DeleteButton;
