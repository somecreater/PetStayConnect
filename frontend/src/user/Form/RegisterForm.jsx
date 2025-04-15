import React, {useState} from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { API_ENDPOINTS } from '../../common/Api/Api';
import TextInput from '../../common/Ui/TextInput';
import Button from '../../common/Ui/Button';
import '../../common/Css/common.css';

function RegisterForm(props){
  
  const [userLoginId, setUserLoginId] = useState('');
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [role, setRole] = useState('CUSTOMER'); 
  const navigate = useNavigate();
  
  const handleRegister = async (e) => {
    e.preventDefault();

    if (password !== confirmPassword) {
        setError('비밀번호와 확인 비밀번호가 일치하지 않습니다.');
        return;
    }

    const payload = {
      userLoginId,
      name,
      email,
      password,
      role,
    };

    try{
      const response = await axios.post(API_ENDPOINTS.auth.register, payload, { withCredentials: true });
      console.log('Registration success:', response.data);  
      navigate('/login');
    } catch (err){
      console.error('Registration failed:', err.response ? err.response.data : err.message);
      setError('회원가입에 실패했습니다. 입력값을 확인해주세요.');
    }

  };
  //일단 간단하게 했지만 추후 좀더 자세하게 구성할 수도
  return(
    <>
      <div>
        <form onSubmit={handleRegister}>
        <TextInput
          classtext="UserLoginIdInput"
          name="userLoginId"
          value={userLoginId}
          placeholderText="아이디를 입력하시오"
          onChange={(e) => setUserLoginId(e.target.value)}
        />
        <TextInput
          classtext="NameInput"
          name="name"
          value={name}
          placeholderText="이름을 입력하시오"
          onChange={(e) => setName(e.target.value)}
        />
        <TextInput
          classtext="EmailInput"
          name="email"
          value={email}
          placeholderText="이메일을 입력하시오"
          onChange={(e) => setEmail(e.target.value)}
        />
        <TextInput
          classtext="PasswordInput"
          name="password"
          value={password}
          placeholderText="비밀번호를 입력하시오"
          onChange={(e) => setPassword(e.target.value)}
        />
        <TextInput
          classtext="ConfirmPasswordInput"
          name="confirmPassword"
          value={confirmPassword}
          placeholderText="비밀번호를 다시 입력하시오"
          onChange={(e) => setConfirmPassword(e.target.value)}
        />
        <div className='SelectRole'>
          <label htmlFor="role">Role: </label>
          <select
            id="role"
            name="role"
            value={role}
            onChange={(e) => setRole(e.target.value)}
          >
            <option value="CUSTOMER">CUSTOMER</option>
            <option value="SERVICE_PROVIDER">SERVICE_PROVIDER</option>
            <option value="MANAGER">MANAGER</option>
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