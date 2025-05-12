import React from 'react';

// 삭제 버튼을 표시하고, 클릭 시 onDelete 콜백을 실행합니다.
function PetDeleteButton({ petId, onDelete }) {
  return (
    <button
      className="btn btn-sm btn-danger"
      onClick={() => onDelete(petId)}
    >
      삭제
    </button>
  );
}

export default PetDeleteButton;
