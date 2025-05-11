import React from 'react';
import ApiService from '../../common/Api/ApiService';
import Button from '../../common/Ui/Button';

export default function ReviewDeleteButton({ reviewId, onDeleted }) {
  const handleDelete = async () => {
    try {
      await ApiService.reviews.delete(reviewId);
      onDeleted();
    } catch (error) {
      console.error('Failed to delete review', error);
    }
  };

  return (
    <div className="ReviewDeleteButton">
      <Button type="button" title="삭제" classtext="DeleteButton" onClick={handleDelete} />
    </div>
  );
}
