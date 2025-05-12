import React, {useState} from 'react';
import { useNavigate } from 'react-router-dom';
import { API_ENDPOINTS } from '../../common/Api/Api';
import ApiService from '../../common/Api/ApiService';
import TextInput from '../../common/Ui/TextInput';
import PasswordInput from '../../common/Ui/PasswordInput';
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
      const response = await ApiService.userService.login({username, password});
      const  { authenticated, LoginUser } = response.data;
      if(authenticated){
        console.log('login success');
        updateUser(LoginUser);
        navigate('/user/info');
      }else{
        console.log('login fail');
      }
    } catch (err) {
      console.error('Login error:', err.response ? err.response.data : err.message);
    }
  };

  const handleGoogleLogin = async (e)=>{
    window.location.href=API_ENDPOINTS.oauth2Google.googleLogin
  };


    return(
    <div className="container py-5" style={{ maxWidth: 400 }}>
      <form className="border p-4 rounded bg-light" onSubmit={handleLogin}>
        <h4 className="mb-4 text-center">로그인</h4>
        <div className="mb-3">
          <TextInput
            classtext="form-control"
            name="username"
            value={username}
            placeholderText="아이디"
            onChange={e => setUsername(e.target.value)}
          />
        </div>
        <div className="mb-4">
          <PasswordInput
            classtext="form-control"
            name="password"
            value={password}
            placeholderText="비밀번호"
            onChange={e => setPassword(e.target.value)}
          />
        </div>
        <div className="d-grid gap-2 mb-3">
          <Button
            classtext="btn btn-primary"
            type="submit"
            onClick={handleLogin}
            title="로그인"
          />
          <Button
            classtext="btn btn-outline-danger"
            type="button"
            onClick={handleGoogleLogin}
            title="Google 로그인"
          />
        </div>
        <div className="text-center">
          <Button
            classtext="btn btn-link"
            type="button"
            onClick={() => navigate('/user/register')}
            title="회원가입"
          />
        </div>
      </form>
    </div>
  );
}

export default LoginForm;
