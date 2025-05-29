import React, {useState} from 'react';
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
  const [isOpen, setIsOpen] = useState(false);
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
      <div className="card-header d-flex justify-content-between align-items-center" style={{ cursor: 'pointer' }} onClick={() => setIsOpen(prev => !prev)}>
        <span>#{id} {name}</span>
        <div>
          <Button
            classtext="btn btn-sm btn-outline-secondary me-2"
            type="button"
            title={isOpen ? '접기' : '펼치기'}
            onClick={(e) => { e.stopPropagation(); setIsOpen(prev => !prev); }}
          />
          {isEdit && (
            <Button
              classtext="btn btn-sm btn-outline-primary"
              type="button"
              title="수정"
              onClick={(e) => { e.stopPropagation(); onEdit(pet); }}
            />
          )}
        </div>
      </div>
      {isOpen && (
        <ul className="list-group list-group-flush">
          {items.map(([label, value]) => (
            <li key={label} className="list-group-item d-flex justify-content-between">
              <strong>{label}</strong><span>{value || '—'}</span>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default Pet;
