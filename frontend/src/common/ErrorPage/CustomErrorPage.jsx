import React, { useState, useEffect }  from "react";
import CusomP from '../Ui/CusomP'
import Button from "../Ui/Button";
import { useNavigate, useParams  } from "react-router-dom";

function CustomErrorPage(props){

  const navigate = useNavigate();
  const [payload,setPayload] = useState({});

  useEffect(() => {
    const stored = localStorage.getItem('lastError');
    if (stored) {

      setPayload(JSON.parse(stored));
      localStorage.removeItem('lastError');

    }
  }, []);
  const { code } = useParams();
  const status = payload.status || `999` ;
  const message = payload.message || `기본 오류`;
  const error = payload.error || `기본 에러`;

  const finalStatus = Number.isInteger(status) ? status : code;

  const titleMap = {
    400: "Bad Request",
    401: "Unauthorized",
    402: "Payment Required",
    403: "Forbidden",
    404: "Page Not Found",
    500: "Server Error",
  };

  const errorSubjectMap = {
    400: "잘못된 데이터 입력 입니다.",
    401: "현재 비로그인 상태 입니다.",
    402: "결제를 요구합니다.",
    403: "해당 작업을 수행할 권한이 없습니다.",
    404: "페이지를 찾을 수 없습니다.",
    500: "서버에서 오류 발생",
  };

  const pageTitle = titleMap[finalStatus] || "Unknown Error";
  const errorSubject = errorSubjectMap[finalStatus] || "오류가 발생했습니다";

  return (
    <>
    <div className="CustomErrorPage">
    <main role="main">
      <section role="alert" aria-labelledby="client_error_title">
        
        <header>
          <h1 className="client_error_title">{pageTitle}</h1>
        </header>

        <div className="ErrorBody">

          <CusomP 
            classtext={'errorMessage'}
            title={errorSubject}
          />

          <CusomP 
            classtext={'errorMessage'}
            title={`${error} : ${message}`}
          />

          <div className="ErrorBottom">

            <Button 
              classText={'LoginButton'} 
              type={'button'} 
              onClick={(e)=>{navigate('/user/login')}} 
              title={'다시 로그인 하기'}
            />

            <Button
              classtext={'HomeButton'} 
              type={'button'} 
              onClick={(e)=>{navigate('/user/info')}} 
              title={'홈으로 돌아가기(아직은 유저 정보 페이지(추후 수정))'}
            />
            
            <Button
              classtext={'HomeButton'} 
              type={'button'} 
              onClick={(e)=>{navigate(-1)}} 
              title={'뒤로가기'}
            />

            </div>
        </div>

      </section>
    </main>
    </div>
    </>
  );
  
}

export default CustomErrorPage;
