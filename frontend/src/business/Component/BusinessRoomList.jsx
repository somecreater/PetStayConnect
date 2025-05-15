import React from 'react';
import BusinessRoom from './BusinessRoom';

function BusinessRoomList(props){
  const { roomList, onRoomSelect, isDelete, onDelete } = props;

  return(
    <div className="container py-4">
      <div
        className="row g-3 overflow-auto"
        style={{ maxHeight: '70vh' }}
      >
        {roomList.map(room => (
          <div
            key={room.id}
            className="col-sm-6 col-md-4"
            onClick={() => onRoomSelect(room)}
            style={{ cursor: 'pointer' }}
          >
            <BusinessRoom
              id={room.id}
              roomType={room.roomType}
              description={room.description}
              roomCount={room.roomCount}
            />
            {isDelete && 
              <button 
                className="btn btn-warning btn-sm"
                type="button"
                onClick={() => onDelete(room.id)}
              >
                방 삭제
              </button>
            }
          </div>
        ))}
      </div>
    </div>
  )
}

export default BusinessRoomList;
