import React from 'react';
import Pet from './Pet';
import PetDeleteButton from './PetDeleteButton';

// 여러 반려동물 정보를 리스트로 표시합니다.
function PetList({ pets, onEdit, onDelete }) {
  return (
    <div className="PetList">
      {pets.map((pet) => (
        <div key={pet.id} className="PetListItem">
          <Pet pet={pet} onEdit={onEdit} />
          <PetDeleteButton petId={pet.id} onDelete={onDelete} />
        </div>
      ))}
    </div>
  );
}

export default PetList;
