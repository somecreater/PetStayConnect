import React, { useState } from 'react';
import CusomP from '../../common/Ui/CusomP';
import Button from '../../common/Ui/Button';
import BookmarkButton from '../../user/component/BookmarkButton';
import BusinessTagList from './BusinessTagList';

function Business({businesssDTO,tagList}){
  
  const [isOpen, setIsOpen] = useState(false);
    const { id,businessName, status, minPrice, maxPrice, facilities, description, avgRate,
        petBusinessTypeName, registrationNumber, province, city, town  } = businesssDTO;
    
    const items = [
    ['상태 ', status],
    ['최저 가격 ', `${minPrice}원`],
    ['최고 가격 ', `${maxPrice}원`],
    ['편의시설 ', facilities],
    ['설명 ', description],
    ['평점 ', avgRate],
    ['주소 ', `${province} ${city} ${town}`],
    ['서비스 타입 ', petBusinessTypeName],
    ['등록번호 ', registrationNumber],
  ];

  return(
    <div className="card mb-3">
      <div
        className="card-header d-flex justify-content-between align-items-center"
        style={{ cursor: 'pointer' }}
        onClick={() => setIsOpen(prev => !prev)}
      >
        <span>#{id} {businessName}</span>
        <BookmarkButton type="BUSINESS_PROVIDER" targetId={id} size={24} className="ms-2"/>
        <Button
          classtext="btn btn-sm btn-outline-secondary"
          type="button"
          title={isOpen ? '접기' : '펼치기'}
          onClick={(e) => { e.stopPropagation(); setIsOpen(prev => !prev); }}
        />
      </div>
      {isOpen && (
        <div>
          <ul className="list-group list-group-flush">
            {items.map(([label, value]) => (
              <li key={label} className="list-group-item d-flex justify-content-between">
                <strong>{label}</strong>
                <span>{value || '—'}</span>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}

export default Business;
