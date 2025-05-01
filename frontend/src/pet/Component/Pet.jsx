import React from 'react';

// 단일 반려동물 정보와 수정 버튼을 표시합니다.
function Pet({ pet, onEdit }) {
  return (
    <div className="Pet">
      <div>이름: {pet.name}</div>
      <div>종류: {pet.type}</div>
      <div>나이: {pet.age}</div>
      <button onClick={() => onEdit(pet)}>수정</button>
    </div>
  );
}

export default Pet;
