import React from 'react';

function BusinessRoom(props){
  
  const {id, roomType, description, roomCount} = props;

  return (
    <div className="card mb-3">
      <div className="card-header">방 #{id}</div>
      <ul className="list-group list-group-flush">
        <li className="list-group-item d-flex justify-content-between">
          <strong>종류</strong><span>{roomType}</span>
        </li>
        <li className="list-group-item d-flex justify-content-between">
          <strong>설명</strong><span>{description || '—'}</span>
        </li>
        <li className="list-group-item d-flex justify-content-between">
          <strong>방 수</strong><span>{roomCount}</span>
        </li>
      </ul>
    </div>
  );
}

export default BusinessRoom;
