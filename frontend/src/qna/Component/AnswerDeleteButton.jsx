import React from 'react';
import ApiService from '../../common/Api/ApiService';
import Button     from '../../common/Ui/Button';

export default function AnswerDeleteButton({ postId, answerId, onDeleted }) {
  const handleDelete = async () => {
    try {
      await ApiService.qnas.answer.delete(postId, answerId);
      onDeleted?.();
    } catch (err) {
      console.error('답변 삭제 실패:', err);
    }
  };

  return <Button title="삭제" onClick={handleDelete} />;
}
