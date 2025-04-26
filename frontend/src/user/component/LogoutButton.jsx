import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useUser } from '../../common/Context/UserContext';
import '../../common/Css/common.css';
import Button from '../../common/Ui/Button';
import ApiService from '../../common/Api/ApiService';

function LogoutButton(props){
    
    const navigate = useNavigate();
    const { resetUser } = useUser();
    const handleLogout = async ()=>{
      try{
        const response = await ApiService.userService.logout();
  
        if(response.data.result){
          alert('로그아웃 되었습니다.');
          resetUser();
          navigate('/user/login');
        }else{
          console.log('로그아웃 실패');
        }
  
      }catch(err){
        console.error('로그아웃 오류:', err);
      }
    };

    return (
        <>
        <Button
          classtext={'LogoutButton'} 
          type={'button'} 
          onClick={handleLogout} 
          title={'로그아웃'}
        />
        </>
    );
  
}

export default LogoutButton;