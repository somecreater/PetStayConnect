import React, { useState } from 'react';
import ApiService          from '../../common/Api/ApiService';
import TextInput           from '../../common/Ui/TextInput';
import Button              from '../../common/Ui/Button';

export default function AnswerRegisterForm({ postId, onSuccess }) {
  const [content, setContent] = useState('');
  const [error, setError]     = useState(null);

  const handleChange = e => {
    setContent(e.target.value);
  };

  const handleSubmit = async e => {
    e.preventDefault();
    setError(null);

    if (!content.trim()) {
      setError('내용을 입력하세요.');
      return;
    }

    try {
      await ApiService.qnas.answer.register(postId, { content });
      setContent('');
      onSuccess?.();
    } catch (err) {
      const msg = err.response?.data || '답변 등록에 실패했습니다.';
      setError(typeof msg === 'string' ? msg : msg.message);
    }
  };

  return (
    <form className="mb-3" onSubmit={handleSubmit}>
      {error && <div className="text-danger mb-2">{error}</div>}
      <div className="mb-3">
        <textarea
            className="form-control form-control-lg mb-3"
            rows={7}
            name="content"
            value={content}
            onChange={handleChange}
            placeholder="답변을 입력하세요"
          />
      </div>
      <div className="d-grid">
        <Button type="submit" title="등록" classtext="btn btn-dark w-100" />
      </div>
    </form>
  );
}

