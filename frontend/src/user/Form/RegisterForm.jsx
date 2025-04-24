import React, {useState} from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { API_ENDPOINTS } from '../../common/Api/Api';
import ApiService from '../../common/Api/ApiService';
import TextInput from '../../common/Ui/TextInput';
import PasswordInput from '../../common/Ui/PasswordInput';
import Button from '../../common/Ui/Button';
import '../../common/Css/common.css';

function RegisterForm(props){
  
  const [registerform,setRegisterform]=useState({
    userLoginId: '',
    name: '',
    email: '',
    phone: '',
    loginType: 'NORMAL',
    petNumber: 0,
    qnaScore: 0,
    point: 0,
    password: '',
    confirmPassword: '',
    role: 'CUSTOMER',
  });

  const [biz, setBiz]=useState({
    businessName:'',
    status:'OPERATION',
    minPrice:0,
    maxPrice:10000000,
    facilities:'',
    description:'',
    avgRate: 0,
    registrationNumber: '',
    bankAccount: '',
    varification: 'NONE',
    userId: '',
    petBusinessTypeId: ''
  });

  const handleChange = (e, isProvider = false) => {
    const { name, value } = e.target;
    isProvider 
    ? setBiz(prev => ({ ...prev, [name]: value })):
    setRegisterform(prev => ({ ...prev, [name]: value }));
  };

  const navigate = useNavigate();
  
  const handleRegister = async (e) => {
    e.preventDefault();

    if (registerform.password !== registerform.confirmPassword) {
        alert('비밀번호와 확인 비밀번호가 일치하지 않습니다.');
        return;
    }

    const payload = {
      ...registerform, 
      petBusinessDTO:
      registerform.role === 'SERVICE_PROVIDER' ? { ...biz, userId: null } : null,
      petDTOList       : [],
      bookmarkDTOList  : [],
      qnaPostDTOList   : [],
      qnaAnswerDTOList : [],
    };

    try{
      const response= ApiService.userService.register(payload);
      alert('회원가입이 완료되었습니다.');
      navigate('/user/login');
    } catch (err){
      console.error('Registration failed:', err.response ? err.response.data : err.message);
      alert('회원가입에 실패했습니다. 입력값을 확인해주세요.');
    }

  };
  
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
        <TextInput
          classtext="PhoneInput"
          name="phone"
          value={registerform.phone}
          placeholderText="전화번호를를 입력하시오"
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
        
        { registerform.role === 'SERVICE_PROVIDER' && (
          <fieldset className="BusinessInfo">
            <legend>사업자 정보</legend>
            <TextInput classtext="BusinessNameInput" name="businessName" value={biz.businessName}
              placeholderText="업체명" onChange={e => handleChange(e, true)} />
            <TextInput classtext="RegistrationNumberInput" name="registrationNumber" value={biz.registrationNumber}
              placeholderText="사업자 등록번호" onChange={e => handleChange(e, true)} />
            <TextInput classtext="BankAccountInput" name="bankAccount" value={biz.bankAccount}
              placeholderText="정산 계좌" onChange={e => handleChange(e, true)} />
            <TextInput classtext="MinPriceInput" name="minPrice" value={biz.minPrice}
              placeholderText="최소 요금" onChange={e => handleChange(e, true)} />
            <TextInput classtext="MaxPriceInput" name="maxPrice" value={biz.maxPrice}
              placeholderText="최대 요금" onChange={e => handleChange(e, true)} />
            <TextInput classtext="FacilitiesInput" name="facilities" value={biz.facilities}
              placeholderText="시설 요약(쉼표 구분)" onChange={e => handleChange(e, true)} />
            <textarea className="DescriptionInput" name="description" value={biz.description}
              placeholder="상세 설명" onChange={e => handleChange(e, true)} />

            <label className="BusinessTypeInput">
              업종 ID(추후 수정):
              <input className="NumberInput" type="number" name="petBusinessTypeId" value={biz.petBusinessTypeId}
                   onChange={e => handleChange(e, true)} />
            </label>

          </fieldset>
        )}

        <Button 
          classtext="RegisterButton" 
          type="submit" 
          title="회원가입(사업자일 시 추가정보 입력 필요)" 
        />
        </form>
      </div>    
    </>
  );

}

export default RegisterForm;
