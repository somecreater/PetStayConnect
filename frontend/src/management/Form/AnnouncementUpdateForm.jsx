import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import ApiService from "../../common/Api/ApiService";
import { useUser } from "../../common/Context/UserContext";

function AnnouncementUpdateForm(props){

  const navigate= useNavigate();
  const {user}= useUser();
  const {announcement, onCancel} = props;
  const [form, setForm]= useState({
    id: announcement.id,
    title: announcement.title,
    type: announcement.type,
    content: announcement.content,
    priority: announcement.priority,
    createdAt: announcement.createdAt,
    updatedAt: announcement.updatedAt,
    userId: announcement.userId,
    userLoginId: announcement.userLoginId
  });

  const handleChange = (e) =>{
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try{
      const response= await ApiService.announce.update(form.id, form);
      const data= response.data;
      if(data.result){
        alert(data.message);
        navigate('/announcements');
      }else{
        alert(data.message);
      }
    } catch (err) {
      alert("공지 수정 도중 오류 발생!");
      console.log(err);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      
      <div className="mb-3">
        <label className="form-label">
          제목
        </label>
        <input
          type="text"
          className="form-control"
          name="title"
          value={form.title}
          onChange={handleChange}
          placeholder={form.title}
        />
      </div>
      
      <div className="mb-3">
        <label className="form-label">
          분류
        </label>
        <input
          type="text"
          className="form-control"
          name="type"
          value={form.type}
          onChange={handleChange}
          placeholder={form.type}
        />
      </div>
      
      <div className="mb-3">
        <label className="form-label">
          우선순위
        </label>
        <select
          className="form-select"
          name="priority"
          value={form.priority}
          onChange={handleChange}
        >
          <option value="GENERAL">일반 공지</option>
          <option value="SPECIAL">중요 공지</option>
          <option value="ETC">기타(오류발생)</option>
        </select>
      </div>
      
      <div className="mb-3">
        <label className="form-label">
          내용
        </label>
        <textarea
          id="content"
          className="form-control"
          name="content"
          rows={5}
          value={form.content}
          onChange={handleChange}
          placeholder={form.content}
        />
      </div>
      
      <div className="mb-3">
        <p className="text-muted small">
          작성자: <span className="fw-semibold">{form.userLoginId}</span> 
        </p>
      </div>

      <div className="d-flex justify-content-end">
        <button
          type="button"
          className="btn btn-secondary me-2"
           onClick={onCancel}
        >
            취소
        </button>
        <button
          className="btn btn-primary"
          type="submit"
        >
          공지 수정
        </button>
      </div>
    </form>
  );
}

export default AnnouncementUpdateForm;