import React from 'react';
import CusomP from '../../common/Ui/CusomP';
import CustomLabel from '../../common/Ui/CustomLabel';
import Button from '../../common/Ui/Button';

// 단일 반려동물 정보와 수정 버튼을 표시합니다.
function Pet({ pet, onEdit, isEdit=false }) {
  const {
    id, name, species, breed,
    age, gender, birthDate,
    healthInfo, createAt, updateAt
  } = pet;

  const items = [
    ['이름 ', name],
    ['종류 ', species],
    ['품종 ', breed],
    ['나이 ', `${age}살`],
    ['성별 ', gender],
    ['생년월일 ', birthDate],
    ['건강 특이사항 ', healthInfo],
    ['생성일 ', createAt],
    ['수정일 ', updateAt],
  ];

  return (
    <div className="card mb-3">
      <div className="card-header d-flex justify-content-between align-items-center">
        펫 #{id}
        {isEdit && (
          <button className="btn btn-sm btn-outline-primary" onClick={() => onEdit(pet)}>
            수정
          </button>
        )}
      </div>
      <ul className="list-group list-group-flush">
        {items.map(([label, value]) => (
          <li key={label} className="list-group-item d-flex justify-content-between">
            <strong>{label}</strong><span>{value || '—'}</span>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default Pet;
