import React from 'react';
import ApiService from '../../common/Api/ApiService';
import Button from '../../common/Ui/Button';

export default function PostDeleteButton({ postId, onDeleted }) {
  const handleDelete = async () => {
    try {
      await ApiService.qnas.post.delete(postId);
      onDeleted();
    } catch (error) {
      console.error('Post delete failed:', error);
    }
  };

  return (
    <div className="d-inline-block">
      <Button
        type="button"
        title="삭제"
        classtext="btn btn-danger btn-sm"
        onClick={handleDelete}
      />
    </div>
  );
}
