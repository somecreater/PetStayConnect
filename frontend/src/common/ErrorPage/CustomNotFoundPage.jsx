import React, { useState, useEffect }  from "react";
import CusomP from '../Ui/CusomP'
import Button from "../Ui/Button";
import { useNavigate } from "react-router-dom";

function CustomNotFoundPage(props){

  const navigate = useNavigate();
  const [payload,setPayload] = useState({});

  useEffect(() => {
    const stored = localStorage.getItem('lastError');
    if (stored) {
      setPayload(JSON.parse(stored));
      localStorage.removeItem('lastError');
    }
  }, []);
  
  const status = payload.status || `404` ;
  const message = payload.message || `기본 오류`;
  const error = payload.error || `기본 에러`;

  return(
    <>
    <div className="CustomErrorPage">
    <main role="main">
      <section role="alert" aria-labelledby="client_error_title">
          
        <header>
          <h1 className="client_error_title">Page Not Found: {status}</h1>
        </header>
  
        <div className="ErrorBody">
          <CusomP 
            classtext={'errorMessage'}
            title="페이지를 찾을 수 없습니다."
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
              title={'로그인 하기'}
            />
            <Button
              classtext={'RegisterButton'} 
              type={'button'} 
              onClick={(e)=>{navigate('/user/register')}} 
              title={'회원가입 하기'}
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

export default CustomNotFoundPage;