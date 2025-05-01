import React from 'react';
import Pet from './Pet';
import Cusomp from '../../common/Ui/CusomP';
import PetDeleteButton from './PetDeleteButton';

// 여러 반려동물 정보를 리스트로 표시합니다.
function PetList({ pets, onEdit, onDelete }) {
  return (
    pets && pets.length>0 ?
    (
    <div className="PetList">
      {pets.map((pet) => (
        <div key={pet.id} className="PetListItem">
          <Pet pet={pet} onEdit={onEdit} />
          <PetDeleteButton petId={pet.id} onDelete={()=>onDelete(pet.id)} />
        </div>
      ))}
    </div>
    )
    :
    (
      <div className="PetList">
        <Cusomp
          classtext={'nothing'}
          title={'펫이 없습니다!!'}
        />
      </div>
    )
    
  );
}

export default PetList;
