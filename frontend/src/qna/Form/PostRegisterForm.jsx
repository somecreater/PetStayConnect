import React, { useState, useContext } from 'react';
import { useNavigate }      from 'react-router-dom';
import ApiService           from '../../common/Api/ApiService';
import TextInput            from '../../common/Ui/TextInput';
import Button               from '../../common/Ui/Button';
import '../../common/Css/common.css';


export default function PostRegisterForm({ onSuccess }) {
  const [form, setForm] = useState({
    title:    '',
    content:  '',
    category: 'GENERAL'
  });
  const [error, setError] = useState(null);
  const navigate          = useNavigate();

  const handleChange = e => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const handleRegister = async e => {
    e.preventDefault();
    setError(null);
    if (!form.title.trim() || !form.content.trim()) {
      setError('제목과 내용을 모두 입력하세요.');
      return;
    }
    try {
      await ApiService.qnas.post.register(form);
      if (onSuccess) onSuccess();
      else navigate('/qnas');
    } catch (err) {
      const msg = err.response?.data || '질문 등록에 실패했습니다.';
      setError(typeof msg === 'string' ? msg : msg.message);
    }
  };

  return (
    <form className="PostRegisterForm" onSubmit={handleRegister}>
      {error && <div className="ErrorMessage">{error}</div>}

      <TextInput
        name="title"
        value={form.title}
        onChange={handleChange}
        placeholderText="제목을 입력하세요"
        classtext="TitleInput"
      />

      <textarea
        name="content"
        value={form.content}
        onChange={handleChange}
        className="ContentInput"
        placeholder="내용을 입력하세요"
      />

      <select
        name="category"
        value={form.category}
        onChange={handleChange}
        className="CategorySelect"
      >
        <option value="GENERAL">GENERAL</option>
        <option value="TECHNICAL">TECHNICAL</option>
        <option value="BUSINESS">BUSINESS</option>
        <option value="ETC">ETC</option>

      </select>

      <Button
        type="submit"
        title="질문 등록"
        classtext="SubmitButton"
      />
    </form>
  );
}
