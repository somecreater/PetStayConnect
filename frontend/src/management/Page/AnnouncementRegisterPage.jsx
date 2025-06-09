import React from "react";
import { useNavigate } from "react-router-dom";
import AnnouncementForm from "../Form/AnnouncementForm";

function AnnouncementRegisterPage(props){
  const navigate= useNavigate();

  const handleCancel = () => {
    navigate('/announcements');
  };

  return (
    <div className="container py-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1 className="h3">새 공지 등록</h1>
        <button className="btn btn-outline-secondary" onClick={handleCancel}>
          취소
        </button>
      </div>

      <div className="card shadow-sm">
        <div className="card-body">
          <AnnouncementForm />
        </div>
      </div>
    </div>
  );
}

export default AnnouncementRegisterPage;