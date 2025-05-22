import React from 'react';
import ApiService from '../../common/Api/ApiService';
import Button from '../../common/Ui/Button';

export default function ReviewDeleteButton({ reviewId, onClick, onDeleted }) {
  const handleDelete = async () => {
    try {
      await ApiService.reviews.delete(reviewId);
      onDeleted();
    } catch (error) {
      console.error('Failed to delete review', error);
    }
  };

  return (
    <div className="d-inline-block">
      <Button
        type="button"
        classtext="btn btn-sm btn-danger"
        title="삭제"
        onClick={handleDelete}
      />
    </div>
  );
}
