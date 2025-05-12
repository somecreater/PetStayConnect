import React, { useEffect } from 'react';
import CusomP from '../../common/Ui/CusomP';

function Business({businesssDTO}){
  
    const { businessName, status, minPrice, maxPrice, facilities, description, avgRate,
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
      <div className="card-header">{businessName}</div>
      <ul className="list-group list-group-flush">
        {items.map(([label, value]) => (
          <li key={label} className="list-group-item d-flex justify-content-between">
            <strong>{label}</strong>
            <span>{value || '—'}</span>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default Business;
