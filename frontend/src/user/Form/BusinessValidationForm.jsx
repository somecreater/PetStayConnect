import React, { useState } from 'react';
import BusinessValidationButton from '../component/BusinessValidationButton';
import TextInput from '../../common/Ui/TextInput';

function BusinessValidationForm(props){

  const [validationform,setValidationform] = useState({
    bNo: '',
    startDt:  '',
    pNm: '',
    pNm2: '',
    bNm: '',
    corpNo: '',
    bSector: '',
    bType: '',
    bAdr: ''
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setValidationform(prev => ({ ...prev, [name]: value }));
  };

  return  (
    <div className="container py-4" style={{ maxWidth: 500 }}>
      <form className="border p-4 rounded bg-light">
        <p className="text-danger mb-3">
          사업자 명이 회원가입시 등록하신 회원 이름과 불일치하면 알림 메일이 안갈 수 있습니다!
        </p>

        {[
          { name: 'bNo', placeholder: '사업자 등록번호(필수)' },
          { name: 'startDt', placeholder: '개업일자(00000000)(필수)' },
          { name: 'pNm', placeholder: '사업자 명(필수)' },
          { name: 'pNm2', placeholder: '사업자 명2' },
          { name: 'bNm', placeholder: '상호' },
          { name: 'corpNo', placeholder: '법인등록번호' },
          { name: 'bSector', placeholder: '업태' },
          { name: 'bType', placeholder: '종목' },
          { name: 'bAdr', placeholder: '사업장주소' },
        ].map(field => (
          <div className="mb-3" key={field.name}>
            <TextInput
              classtext="form-control"
              name={field.name}
              value={validationform[field.name]}
              placeholderText={field.placeholder}
              onChange={handleChange}
            />
          </div>
        ))}

        <div className="text-center">
          <BusinessValidationButton {...validationform} />
        </div>
      </form>
    </div>
  );
}

export default BusinessValidationForm;
