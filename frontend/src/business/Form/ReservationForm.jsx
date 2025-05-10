import React, { useContext, useState } from 'react';
import BusinessRoomList from '../Component/BusinessRoomList';
import PetList from '../../pet/Component/PetList';

function ReservationForm(props){

  const { user_login_id, business_register_number, roomList, petList, business } = props;
  const [selectedRoom, setSelectedRoom] = useState(null);
  const [selectedPet, setSelectedPet] =useState([]);

  const [reservationForm, setReservationForm]= useState({
    user_login_id: user_login_id || '',
    business_register_number: business_register_number || '',
    roomType: '',
    checkIn:'',
    checkOut: '',
    specialRequests:'',
    businessRequestInfo: '',
    petDTOList: []
  });  

  const handleChange = (e) => {
    const { name, value } = e.target;
    setReservationForm(prev => ({ ...prev, [name]: value }));
  };

  const handlePetSelected = (pet) => {
    console.log('선택된 애완동물: ', pet);
    if (!selectedPet.find(p => p.id === pet.id)) {
      const updatedPets = [...selectedPet, pet];
      setSelectedPet(updatedPets);
      setReservationForm(prev => ({ ...prev, petDTOList: updatedPets }));
    }
  }

  const handleRoomSelect = (room) => {
    console.log('선택된 방:', room);
    setSelectedRoom(room);
    setReservationForm(prev => ({ ...prev, roomType: room.roomType }));
  };

  const handleSubmit = async ()=>{

  }

  //예약 폼
  return (
    <>
      <form className='ReservationForm'>
        <label className='Reservation_label' htmlFor='Reservation_info'>회원님 아이디</label>
        <p className='Reservation_info'>{user_login_id}</p>

        <label className='Reservation_label' htmlFor='Reservation_info'>사업체 이름</label>
        <p className='Reservation_info'>{business.businessName}</p>

        <label className='Reservation_label' htmlFor='Reservation_info'>사업자 번호</label>
        <p className='Reservation_info'>{business_register_number}</p>

        <label className='Reservation_label' htmlFor='Reservation_info'>설명(요구 정보)</label>
        <p className='Reservation_info'>{business.description}</p>

        <label className='Reservation_label' htmlFor='Reservation_info'>편의 시설</label>
        <p className='Reservation_info'>{business.facilities}</p>

        <div className='Reservation'>

          { petList && petList.length > 0 ? (
            <>
              <p className='Reservation_info'>애완동물 선택</p>
              <PetList pets={petList} selected={handlePetSelected}/>
            </>
            ) : (
            <p className='Reservation_info'>회원님의 애완동물을 등록해주세요!</p>
          )}
          
          {roomList && roomList.length > 0 &&
            <>
              <p className='Reservation_info'>방 선택</p>
              <BusinessRoomList List={roomList} onRoomSelect={handleRoomSelect} />
            </>
          }

          <label htmlFor="Reservation_info">체크인 날짜:</label>
          <input 
            type="date" 
            className='Reservation_info'
            name="checkIn" 
            value={reservationForm.checkIn} 
            onChange={handleChange} 
          />

          <label htmlFor="Reservation_info">체크아웃 날짜:</label>
          <input 
            type="date" 
            className='Reservation_info'
            name="checkOut" 
            value={reservationForm.checkOut} 
            onChange={handleChange} 
          />

          <label htmlFor="Reservation_info">특별 요구사항:</label>
          <input
            type="text" 
            className='Reservation_info'
            name="specialRequests" 
            value={reservationForm.specialRequests} 
            onChange={handleChange} 
          />

          <label htmlFor="Reservation_info">사업자 요구정보:</label>
          <input
            type="text" 
            className='Reservation_info'
            name="businessRequestInfo" 
            value={reservationForm.businessRequestInfo} 
            onChange={handleChange} 
          />

        </div>
        <button className='Reservation_button' type='button' onClick={() => handleSubmit}>
          예약 하기
        </button>
      </form>
    </>
  );
}

export default ReservationForm;