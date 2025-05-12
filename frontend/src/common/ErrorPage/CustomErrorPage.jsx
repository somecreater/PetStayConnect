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
    <div className="container py-5">
      <div className="row justify-content-center">
        <div className="col-lg-8">
          <div className="card border-danger">
            <div className="card-header bg-danger text-white text-center">
              <h1 className="m-0">{status} – {pageTitle}</h1>
            </div>
            <div className="card-body text-center">
              <p className="lead">{errorSubject}</p>
              <CusomP
                classtext="text-muted mb-3"
                title={`${error} : ${message}`}
              />
              <div className="d-flex justify-content-center gap-2 flex-wrap">
                <Button
                  type="button"
                  classtext="btn btn-outline-primary"
                  title="다시 로그인하기"
                  onClick={() => navigate("/user/login")}
                />
                <Button
                  type="button"
                  classtext="btn btn-outline-secondary"
                  title="홈으로 돌아가기"
                  onClick={() => navigate("/user/info")}
                />
                <Button
                  type="button"
                  classtext="btn btn-outline-dark"
                  title="뒤로가기"
                  onClick={() => navigate(-1)}
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
  
}

export default CustomErrorPage;
