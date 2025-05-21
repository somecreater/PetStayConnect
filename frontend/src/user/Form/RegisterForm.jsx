import React, {useEffect, useState} from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import CustomP from '../../common/Ui/CusomP';
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
    province: '',
    city: '',
    town: '',
    userId: '',
    petBusinessTypeId: ''
  });
  
  const isProvider = registerform.role === 'SERVICE_PROVIDER';

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

  return (
    <div className="container py-4">
      <form onSubmit={handleRegister} className="border p-4 rounded bg-light">
        <h4 className="mb-4">회원가입</h4>

        <div className="mb-3">
          <label className="form-label">아이디</label>
          <TextInput
            classtext="form-control"
            name="userLoginId"
            value={registerform.userLoginId}
            placeholderText="아이디를 입력하세요"
            onChange={handleChange}
          />
        </div>

        <div className="row g-3">
          <div className="col-md-6">
            <label className="form-label">이름</label>
            <TextInput
              classtext="form-control"
              name="name"
              value={registerform.name}
              placeholderText="이름"
              onChange={handleChange}
            />
          </div>
          <div className="col-md-6">
            <label className="form-label">이메일</label>
            <TextInput
              classtext="form-control"
              name="email"
              value={registerform.email}
              placeholderText="이메일"
              onChange={handleChange}
            />
          </div>
        </div>

        <div className="mb-3 mt-3">
          <label className="form-label">전화번호</label>
          <TextInput
            classtext="form-control"
            name="phone"
            value={registerform.phone}
            placeholderText="전화번호"
            onChange={handleChange}
          />
        </div>

        <div className="row g-3">
          <div className="col-md-6">
            <label className="form-label">비밀번호</label>
            <PasswordInput
              classtext="form-control"
              name="password"
              value={registerform.password}
              placeholderText="비밀번호"
              onChange={handleChange}
            />
          </div>
          <div className="col-md-6">
            <label className="form-label">비밀번호 확인</label>
            <PasswordInput
              classtext="form-control"
              name="confirmPassword"
              value={registerform.confirmPassword}
              placeholderText="비밀번호 확인"
              onChange={handleChange}
            />
          </div>
        </div>

        <div className="mb-3 mt-3">
          <label className="form-label">Role</label>
          <select
            className="form-select"
            name="role"
            value={registerform.role}
            onChange={handleChange}
          >
            <option value="CUSTOMER">일반 회원</option>
            <option value="SERVICE_PROVIDER">사업자</option>
            <option value="MANAGER">관리자</option>
          </select>
        </div>

        {isProvider && (
          <fieldset className="border rounded p-3 mb-4">
            <legend className="float-none w-auto px-2">사업자 정보</legend>

            <div className="mb-3">
              <label className="form-label">업체명</label>
              <TextInput
                classtext="form-control"
                name="businessName"
                value={biz.businessName}
                placeholderText="업체명"
                onChange={e => handleChange(e, true)}
              />
            </div>

            <div className="row g-3">
              <div className="col-md-6">
                <label className="form-label">등록번호</label>
                <TextInput
                  classtext="form-control"
                  name="registrationNumber"
                  value={biz.registrationNumber}
                  placeholderText="사업자 등록번호"
                  onChange={e => handleChange(e, true)}
                />
              </div>
              <div className="col-md-6">
                <label className="form-label">계좌번호</label>
                <TextInput
                  classtext="form-control"
                  name="bankAccount"
                  value={biz.bankAccount}
                  placeholderText="정산 계좌"
                  onChange={e => handleChange(e, true)}
                />
              </div>
            </div>

            <div className="row g-3 mt-3">
              <div className="col-md-4">
                <label className="form-label">최소 요금</label>
                <TextInput
                  classtext="form-control"
                  name="minPrice"
                  value={biz.minPrice}
                  placeholderText="최소 요금"
                  onChange={e => handleChange(e, true)}
                />
              </div>
              <div className="col-md-4">
                <label className="form-label">최대 요금</label>
                <TextInput
                  classtext="form-control"
                  name="maxPrice"
                  value={biz.maxPrice}
                  placeholderText="최대 요금"
                  onChange={e => handleChange(e, true)}
                />
              </div>
              <div className="col-md-4">
                <label className="form-label">업종 ID</label>
                <CustomP classtext="mb-0" title={'회원정보 수정시 선택'} />
              </div>
            </div>

            <div className="mb-3 mt-3">
              <label className="form-label">주소</label>
              <div className="row g-2">
                <div className="col">
                  <TextInput
                    classtext="form-control"
                    name="province"
                    value={biz.province}
                    placeholderText="도"
                    onChange={e => handleChange(e, true)}
                  />
                </div>
                <div className="col">
                  <TextInput
                    classtext="form-control"
                    name="city"
                    value={biz.city}
                    placeholderText="시"
                    onChange={e => handleChange(e, true)}
                  />
                </div>
                <div className="col">
                  <TextInput
                    classtext="form-control"
                    name="town"
                    value={biz.town}
                    placeholderText="동"
                    onChange={e => handleChange(e, true)}
                  />
                </div>
              </div>
            </div>

            <div className="mb-3">
              <label className="form-label">시설 요약</label>
              <TextInput
                classtext="form-control"
                name="facilities"
                value={biz.facilities}
                placeholderText="쉼표로 구분"
                onChange={e => handleChange(e, true)}
              />
            </div>

            <div className="mb-3">
              <label className="form-label">상세 설명</label>
              <textarea
                className="form-control"
                name="description"
                rows={3}
                value={biz.description}
                onChange={e => handleChange(e, true)}
              />
            </div>
          </fieldset>
        )}

        <div className="d-grid">
          <Button
            classtext="btn btn-primary"
            type="submit"
            title="회원가입"
          />
        </div>
      </form>
    </div>
  );

}

export default RegisterForm;
