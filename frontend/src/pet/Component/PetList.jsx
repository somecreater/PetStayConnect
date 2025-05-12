import React from 'react';
import Pet from './Pet';
import Cusomp from '../../common/Ui/CusomP';
import PetDeleteButton from './PetDeleteButton';

// 여러 반려동물 정보를 리스트로 표시합니다.
function PetList({ pets, onEdit, isEdit, onDelete, isDelete, selected }) {

  const handleSelect = (pet) => {
    selected(pet);
  }

  return (
    <div className="container py-4">
      {pets && pets.length > 0 ? (
        <div
          className="row g-4 overflow-auto"
          style={{ maxHeight: '70vh' }}
        >
          {pets.map(pet => (
            <div
              key={pet.id}
              className="col-sm-6 col-md-4"
              onClick={() => handleSelect(pet)}
              style={{ cursor: 'pointer' }}
            >
              <div className="card h-100">
                <div className="card-body d-flex flex-column">
                  <Pet 
                    pet={pet} 
                    isEdit={isEdit} 
                    onEdit={onEdit} 
                  />
                  {isDelete && (
                    <PetDeleteButton
                      petId={pet.id}
                      onDelete={() => onDelete(pet.id)}
                      className="btn btn-danger mt-auto"
                    />
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <div className="alert alert-info text-center">
          <Cusomp classtext="mb-0" title="펫이 없습니다!!" />
        </div>
      )}
    </div>
  );
}

export default PetList;
