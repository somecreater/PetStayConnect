import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ApiService from '../../common/Api/ApiService';
import TextInput from '../../common/Ui/TextInput';
import Button from '../../common/Ui/Button';

export default function PostUpdateForm({ initialData, onSuccess }) {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    title:    initialData.title   || '',
    content:  initialData.content || '',
    category: initialData.category || '',
  });
  const [error, setError] = useState(null);

  const handleChange = e => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async e => {
    e.preventDefault();
    setError(null);

    if (!form.title.trim() || !form.content.trim()) {
      setError('제목과 내용을 모두 입력하세요.');
      return;
    }

    try {
      await ApiService.qnas.post.update(initialData.id, form);
      onSuccess?.();
      navigate(`/qnas/${initialData.id}`);
    } catch (err) {
      const msg = err.response?.data || '수정에 실패했습니다.';
      setError(typeof msg === 'string' ? msg : msg.message);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      {error && <div className="text-danger mb-3">{error}</div>}
      <div className="mb-3">
        <TextInput
          classtext="form-control"
          name="title"
          value={form.title}
          onChange={handleChange}
          placeholderText="제목을 수정하세요"
        />
      </div>
      <div className="mb-3">
        <textarea
          className="form-control"
          name="content"
          value={form.content}
          onChange={handleChange}
          placeholder="내용을 수정하세요"
          rows={6}
        />
      </div>
      <div className="mb-3">
        <select
          className="form-select"
          name="category"
          value={form.category}
          onChange={handleChange}
        >
          <option value="">카테고리 선택</option>
          <option value="GENERAL">GENERAL</option>
          <option value="TECHNICAL">TECHNICAL</option>
          <option value="BUSINESS">BUSINESS</option>
          <option value="ETC">ETC</option>
        </select>
      </div>
      <div className="d-grid">
        <Button type="submit" title="수정" classtext="btn btn-primary" />
      </div>
    </form>
);
}
