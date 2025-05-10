import React from 'react';
import BusinessRoom from './BusinessRoom';

function BusinessRoomList(props){
  const { roomList, onRoomSelect } = props;

  const handleSelect = (room) => {
    onRoomSelect(room);
  };

  return (
    <>
    <div className='BusinessRoomList'>
      {roomList.map(room =>(
        <div key={room.id} onClick={() => handleSelect(room)}>
          <BusinessRoom 
            id={room.id} 
            roomType={room.roomType} 
            description={room.description}
          />
        </div>
      ))}
    </div>
    </>
  );
}

export default BusinessRoomList;