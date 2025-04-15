import React, {useState} from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { API_ENDPOINTS, createHeaders   } from '../../common/Api/Api';
import RefreshApi from '../../common/Api/RefreshApi';
import TextInput from '../../common/Ui/TextInput';
import Button from '../../common/Ui/Button';
import { useUser } from '../../common/Context/UserContext';
import '../../common/Css/common.css';

function LoginForm(props){

  const [username,setUsername]= useState('');
  const [password,setPassword]= useState('');
  const { updateUser  } = useUser();
  const navigate = useNavigate();


  const handleLogin = async (e)=>{
    e.preventDefault();
    try{
      const response = await axios.post(API_ENDPOINTS.auth.login,
        { username, password },
        { withCredentials: true }
      );
      if(response.data.authenticated){
        console.log('login success');
        getuserInfo(response.data.LoginId);
      }else{
        console.log('login fail');
      }
    } catch (err) {
      console.error('Login error:', err.response ? err.response.data : err.message);
    }
  }

  const getuserInfo = async (UserId) => {
    try{
      const response = await RefreshApi.get(API_ENDPOINTS.auth.detail, {
        params: { userLoginId: UserId },
        headers: createHeaders(),
      });
      
      if(response.data.auth){
        console.log('get user');
        updateUser({
          name: response.data.userDetail.name,
          email: response.data.userDetail.email,
          userLoginId: response.data.userDetail.userLoginId,
          role: response.data.userDetail.role, 
        });
        navigate('/info');
      }else{
        console.log('사용자 정보 없음');
      }
    }catch(err){
      console.error('사용자 정보 가져오기 오류:', err);
    }

  }
    return(
        <>
          <div>
            <form onSubmit={handleLogin}>
              <TextInput 
                classtext={'LoginIdInput'}
                name={'username'} 
                value={username} 
                placeholderText={'아이디를 입력하시오'} 
                onChange={(e)=>{setUsername(e.target.value)}}
              />
              <TextInput
                classtext={'PasswordInput'}
                name={'password'} 
                value={password} 
                placeholderText={'비밀번호를 입력하시오'} 
                onChange={(e)=>{setPassword(e.target.value)}}
              />
              <Button 
                classtext={'LoginButton'} 
                type={'submit'} 
                onClick={handleLogin} 
                title={'로그인'}/>
              <Button
                classtext={'RegisterButton'} 
                type={'button'} 
                onClick={(e)=>{navigate('/register')}} 
                title={'회원가입'}/>
            </form>
          </div>
        </>
    );
}

export default LoginForm;