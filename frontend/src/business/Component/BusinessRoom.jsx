import React from 'react';

function BusinessRoom(props){
  
  const {id, roomType, description} = props;

  return (
    <>
      <div className='BusinessRoom'>
        <label className='infoLabel' htmlFor='info'>아이디</label>
        <p className='info'>{id}</p>

        <label className='infoLabel' htmlFor='info'>방 종류</label>
        <p className='info'>{roomType}</p>
        
        <label className='infoLabel' htmlFor='info'>설명</label>
        <p className='info'>{description}</p>
      </div>
    </>
  );
}

export default BusinessRoom;