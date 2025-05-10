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

  return (
    <>
      <form className='BusinessValidationForm'>
        <p className='BusinessValidation'>
          사업자 명이 회원가입시 등록하신 회원 이름과 불일치하면 알림 메일이 안갈 수 있습니다!!
        </p>
        <TextInput
          classtext="BusinessValidation"
          name="bNo"
          value={validationform.bNo}
          placeholderText="사업자 등록번호(필수)"
          onChange={handleChange}
        />
        <TextInput
          classtext="BusinessValidation"
          name="startDt"
          value={validationform.startDt}
          placeholderText="개업일자(00000000)(필수)"
          onChange={handleChange}
        />
        <TextInput
          classtext="BusinessValidation"
          name="pNm"
          value={validationform.pNm}
          placeholderText="사업자 명(필수)"
          onChange={handleChange}
        />
        <TextInput
          classtext="BusinessValidation"
          name="pNm2"
          value={validationform.pNm2}
          placeholderText="사업자 명2"
          onChange={handleChange}
        />
        <TextInput
          classtext="BusinessValidation"
          name="bNm"
          value={validationform.bNm}
          placeholderText="상호"
          onChange={handleChange}
        />
        <TextInput
          classtext="BusinessValidation"
          name="corpNo"
          value={validationform.corpNo}
          placeholderText="법인등록번호"
          onChange={handleChange}
        />
        <TextInput
          classtext="BusinessValidation"
          name="bSector"
          value={validationform.bSector}
          placeholderText="업태"
          onChange={handleChange}
        />
        <TextInput
          classtext="BusinessValidation"
          name="bType"
          value={validationform.bType}
          placeholderText="종목"
          onChange={handleChange}
        />
        <TextInput
          classtext="BusinessValidation"
          name="bAdr"
          value={validationform.bAdr}
          placeholderText="사업장주소"
          onChange={handleChange}
        />

        <BusinessValidationButton 
          bNo={validationform.bNo} 
          startDt={validationform.startDt} 
          pNm={validationform.pNm} 
          pNm2={validationform.pNm2} 
          bNm={validationform.bNm} 
          corpNo={validationform.corpNo} 
          bSector={validationform.bSector} 
          bType={validationform.bType} 
          bAdr={validationform.bAdr}
        />
      </form>
    </>
  );
}

export default BusinessValidationForm;