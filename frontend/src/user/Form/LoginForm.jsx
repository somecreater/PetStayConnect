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
        <>
          <div>
            <form className='UserLoginForm' onSubmit={handleLogin}>
              <TextInput 
                classtext={'LoginIdInput'}
                name={'username'} 
                value={username} 
                placeholderText={'아이디를 입력하시오'} 
                onChange={(e)=>{setUsername(e.target.value)}}
              />
              <PasswordInput
                classtext={'PasswordInput'}
                name={'password'} 
                value={password} 
                placeholderText={'비밀번호를 입력하시오'} 
                onChange={(e)=>{setPassword(e.target.value)}}
              />
              <br/>
              <div className='LoginFormBottom'>
                <Button 
                  classtext={'GoogleLoginButton'}
                  type={'button'}
                  onClick={handleGoogleLogin}
                  title={'Google Login'}/>
                <Button 
                  classtext={'LoginButton'} 
                  type={'submit'} 
                  onClick={handleLogin} 
                  title={'로그인'}/>
                <Button
                  classtext={'RegisterButton'} 
                  type={'button'} 
                  onClick={(e)=>{navigate('/user/register')}} 
                  title={'회원가입'}/>
              </div>
            </form>
          </div>
        </>
    );
}

export default LoginForm;