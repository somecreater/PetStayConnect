import React, { useState } from "react";
import { useUser } from "../../common/Context/UserContext";
import ApiService from "../../common/Api/ApiService";
import { useNavigate } from "react-router-dom";

function AnnouncementForm(props){
  
  const navigate= useNavigate();
  const {user} = useUser();
  const [form,setForm]= useState({
    id: null,
    title: '',
    type: '일반',
    content: '',
    priority: 'GENERAL',
    createdAt: null,
    updatedAt: null,
    userId: user?.id,
    userLoginId: user?.userLoginId
  });

  const handleChange = (e) =>{
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try{
      const response = await ApiService.announce.register(form);
      const data = response.data;
      if(data.result){
        alert(data.message);
        navigate('/announcements');
      }else{
        alert(data.message);
      }
    }catch (err){
      alert("공지 등록 도중 오류 발생!");
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
          placeholder="제목"
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
          placeholder="분류"
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
          placeholder="내용을 적어주세요."
        />
      </div>

      <div className="mb-3">
        <p className="text-muted small">
          작성자: <span className="fw-semibold">{form.userLoginId}</span> 
        </p>
      </div>

      <div className="d-grid">
        <button
          className="btn btn-primary"
          type="submit"
        >
          공지 등록
        </button>
      </div>

    </form>
  );
}

export default AnnouncementForm;