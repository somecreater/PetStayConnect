import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useUser } from '../../common/Context/UserContext';
import { API_ENDPOINTS, createHeaders   } from '../../common/Api/Api';
import RefreshApi from '../../common/Api/RefreshApi';
import '../../common/Css/common.css';
import Button from '../../common/Ui/Button';
import TextInput from '../../common/Ui/TextInput';
import PasswordInput from '../../common/Ui/PasswordInput';

function UserDeleteForm(props){
  
  const navigate=useNavigate();
  const {user,resetUser} =useUser();  
  const [userid, setUserid]=useState('');
  const [userpass,setUserpass]=useState('');

  const handleDeleteUser= async (e) => {
    e.preventDefault();

    try{
      const response = await RefreshApi.post(
        API_ENDPOINTS.auth.delete,
        { 
          LoginId: userid, 
          Password: userpass,
        },
        { 
          withCredentials: true, 
          headers: createHeaders(),
        }
    );

      if(response.data.result){
        alert('회원 탈퇴가 완료되었습니다.');
        resetUser();
        navigate('/user/login');
      }else{
        alert('회원 탈퇴에 실패하였습니다.');
        navigate('/user/login');
      }
      
    }catch(err){
      alert('회원 탈퇴 중 오류가 발생하였습니다.');
      navigate('/user/login');
      console.log(err);
      
    }
  }
  return (
    <>
    <form className="UserDeleteForm" onSubmit={handleDeleteUser}>
      <TextInput 
        classtext={'LoginIdInput'}
        name={'userid'} 
        value={userid} 
        placeholderText={'아이디를 입력하시오'} 
        onChange={(e)=>{setUserid(e.target.value)}}
      />
      <PasswordInput 
        classtext={'LoginIdInput'}
        name={'userpass'} 
        value={userpass} 
        placeholderText={'비밀번호를 입력하시오'} 
        onChange={(e)=>{setUserpass(e.target.value)}}
      />
      <Button
        classtext="UserDeleteButton"
        type="submit" 
        title="회원 탈퇴" 
      />
    </form>
    </>
  );
}

export default UserDeleteForm;