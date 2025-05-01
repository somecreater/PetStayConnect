import React, { useState } from 'react';
import { useNavigate }    from 'react-router-dom';
import ApiService         from '../../common/Api/ApiService';
import TextInput          from '../../common/Ui/TextInput';
import Button             from '../../common/Ui/Button';

export default function PostUpdateForm({ initialData, onSuccess }) {
  const [form, setForm]   = useState({
    title: initialData.title,
    content: initialData.content,
    category: initialData.category
  });
  const [error, setError] = useState(null);
  const navigate          = useNavigate();

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
      {error && <div>{error}</div>}

      <TextInput
        name="title"
        value={form.title}
        onChange={handleChange}
        placeholderText="제목을 수정하세요"
      />

      <textarea
        name="content"
        value={form.content}
        onChange={handleChange}
        placeholder="내용을 수정하세요"
        rows={6}
      />

      <select name="category" value={form.category} onChange={handleChange}>
        <option value="GENERAL">GENERAL</option>
        <option value="TECHNICAL">TECHNICAL</option>
        <option value="OTHER">OTHER</option>
      </select>

      <Button type="submit" title="수정" />
    </form>
  );
}
