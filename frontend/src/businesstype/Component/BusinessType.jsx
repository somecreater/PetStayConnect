import React from "react";
import CusomP from '../../common/Ui/CusomP';
import CustomLabel from "../../common/Ui/CustomLabel";
function BusinessType(props){
  
  const {id, sectorCode, typeCode, typeName, description} = props;

  const items = [
    ['타입 이름', typeName],
    ['섹터 코드', sectorCode],
    ['타입 코드', typeCode],
    ['설명', description],
  ];

  return (
    <div className="card mb-3">
      <div className="card-header">타입 #{id}</div>
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

export default BusinessType;
