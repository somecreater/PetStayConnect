import React from 'react';
import BusinessRoom from './BusinessRoom';

function BusinessRoomList(props){
  const { roomList, onRoomSelect } = props;

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
            />
          </div>
        ))}
      </div>
    </div>
  )
}

export default BusinessRoomList;
