import React from 'react';
import ApiService from '../../common/Api/ApiService';
import Button from '../../common/Ui/Button';

export default function AnswerDeleteButton({ postId, answerId, onDeleted }) {
  const handleDelete = async () => {
    try {
      await ApiService.qnas.answer.delete(postId, answerId);
      onDeleted();
    } catch (error) {
      console.error('Answer delete failed:', error);
    }
  };

  return (
    <div className="DeleteButtonWrapper">
      <Button
        type="button"
        title="삭제"
        classtext="DeleteButton"
        onClick={handleDelete}
      />
    </div>
  );
}
