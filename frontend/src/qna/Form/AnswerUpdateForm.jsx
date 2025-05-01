import React, { useState } from 'react';
import ApiService         from '../../common/Api/ApiService';
import Button             from '../../common/Ui/Button';
import TextInput          from '../../common/Ui/TextInput';

export default function AnswerUpdateForm({ postId, answer, onSuccess }) {
  const [content, setContent] = useState(answer.content);
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
       await ApiService.qnas.answer.update(postId, answer.id, { content });
       onSuccess?.();
     } catch (err) {
       const msg = err.response?.data || '수정에 실패했습니다.';
       setError(typeof msg === 'string' ? msg : msg.message);
     }
   };

   return (
     <form onSubmit={handleSubmit}>
       {error && <div>{error}</div>}

       <TextInput
         name="content"
         value={content}
         onChange={handleChange}
         placeholderText="답변을 수정하세요"
       />

       <Button type="submit" title="수정" />
     </form>
   );
 }