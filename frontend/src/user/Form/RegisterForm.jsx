import React, {useState} from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { API_ENDPOINTS } from '../../common/Api/Api';
import TextInput from '../../common/Ui/TextInput';
import PasswordInput from '../../common/Ui/PasswordInput';
import Button from '../../common/Ui/Button';
import '../../common/Css/common.css';

function RegisterForm(props){
  
  const [registerform,setRegisterform]=useState({
    userLoginId: '',
    name: '',
    email: '',
    password: '',
    confirmPassword: '',
    role: 'CUSTOMER',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setRegisterform(prev => ({ ...prev, [name]: value }));
  };
  const navigate = useNavigate();
  
  const handleRegister = async (e) => {
    e.preventDefault();

    if (registerform.password !== registerform.confirmPassword) {
        alert('비밀번호와 확인 비밀번호가 일치하지 않습니다.');
        return;
    }

    const { userLoginId, name, email, password, role } = registerform;
    const payload = { userLoginId, name, email, password, role };

    try{
      const response = await axios.post(API_ENDPOINTS.auth.register, payload, { withCredentials: true });
      navigate('/user/login');
    } catch (err){
      console.error('Registration failed:', err.response ? err.response.data : err.message);
      alert('회원가입에 실패했습니다. 입력값을 확인해주세요.');
    }

  };
  //일단 간단하게 했지만 추후 좀더 자세하게 구성할 수도
  return(
    <>
      <div>
        <form className="UserRegisterForm" onSubmit={handleRegister}>
        <TextInput
          classtext="UserLoginIdInput"
          name="userLoginId"
          value={registerform.userLoginId}
          placeholderText="아이디를 입력하시오"
          onChange={handleChange}
        />
        <TextInput
          classtext="NameInput"
          name="name"
          value={registerform.name}
          placeholderText="이름을 입력하시오"
          onChange={handleChange}
        />
        <TextInput
          classtext="EmailInput"
          name="email"
          value={registerform.email}
          placeholderText="이메일을 입력하시오"
          onChange={handleChange}
        />
        <PasswordInput
          classtext="PasswordInput"
          name="password"
          value={registerform.password}
          placeholderText="비밀번호를 입력하시오"
          onChange={handleChange}
        />
        <PasswordInput
          classtext="ConfirmPasswordInput"
          name="confirmPassword"
          value={registerform.confirmPassword}
          placeholderText="비밀번호를 다시 입력하시오"
          onChange={handleChange}
        />
        <div className='SelectRole'>
          <label htmlFor="role">Role: </label>
          <select
            id="role"
            name="role"
            value={registerform.role}
            onChange={handleChange}
          >
            <option value="CUSTOMER">일반 회원</option>
            <option value="SERVICE_PROVIDER">사업자</option>
            <option value="MANAGER">관리자</option>
          </select>
        </div>
        <Button 
          classtext="RegisterButton" 
          type="submit" 
          title="회원가입" 
        />
        </form>
      </div>    
    </>
  );

}

export default RegisterForm;