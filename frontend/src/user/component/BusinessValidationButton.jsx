import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useUser } from '../../common/Context/UserContext';
import '../../common/Css/common.css';
import Button from '../../common/Ui/Button';
import ApiService from '../../common/Api/ApiService';

function BusinessValidationButton(props){

  const { user } = useUser();
  const {bNo, startDt, pNm, pNm2, bNm, corpNo, bSector, bType, bAdr} = props;
  const request={
    b_no: bNo || user.petBusinessDTO.registrationNumber,
    start_dt: startDt || '',
    p_nm: pNm || user.name,
    p_nm2: pNm2 || '',
    b_nm: bNm || '',
    corp_no: corpNo || '',
    b_sector: bSector || '',
    b_type: bType || '',
    b_adr: bAdr || ''
  };

  const validationRequest= async () => {
    console.log(request);
    const response = await ApiService.businessvalidation.validation(request);
    const data= response.data;

    if(data.result){
      alert(data.message+'\n 보낸 요청 요약: '
        + '\n사업자 번호: ' + request.b_no
        + '\n 개업 일자: ' + request.start_dt
        + '\n사업자 명: ' + request.p_nm);
    }else{
      alert(data.message);
    }
  }

  return (
    <Button
      classtext="btn btn-success w-100"
      type="button"
      onClick={validationRequest}
      title="사업자 인증"
    />
  );

}

export default BusinessValidationButton;
