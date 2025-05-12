import React, { useCallback, useContext, useEffect, useState } from 'react';
import BusinessRoomList from '../Component/BusinessRoomList';
import PetList from '../../pet/Component/PetList';
import ApiService from '../../common/Api/ApiService';
import Button from '../../common/Ui/Button';

function ReservationForm(props){

  const { user_login_id, business_register_number, petList, business } = props;
  const [selectedRoom, setSelectedRoom] = useState(null);
  const [selectedPet, setSelectedPet] =useState([]);
  const [roomList, setRoomList]= useState([]);
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

  //추후 백엔드 단에서 구현
  const fetchRooms= useCallback(async () => {
    const res= await ApiService.businessService.roomList(business_register_number);
    const data=res.data;
    if(data.result){
      setRoomList(data.rooms);
    }else{
      setRoomList([]);
    }

  },[business]);

  useEffect(()=>{
    fetchRooms();
  },[fetchRooms]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setReservationForm(prev => ({ ...prev, [name]: value }));
  };

  const handlePetSelected = (pet) => {
    console.log('선택된 애완동물: ', pet);
    let updatedPets;
    if (selectedPet.find(p => p.id === pet.id)) {
      updatedPets = selectedPet.filter(p => p.id !== pet.id);
    }else{
      updatedPets = [...selectedPet, pet];
    }
    setSelectedPet(updatedPets);
    setReservationForm(prev => ({ ...prev, petDTOList: updatedPets }));
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
    <div className="container py-4">
      <form onSubmit={e => { e.preventDefault(); handleSubmit(); }}>
        {/* Read‐only Info */}
        <div className="row mb-3">
          <div className="col-sm-6">
            <label className="form-label">회원님 아이디</label>
            <p className="form-control-plaintext">{user_login_id}</p>
          </div>
          <div className="col-sm-6">
            <label className="form-label">사업체 이름</label>
            <p className="form-control-plaintext">{business.businessName}</p>
          </div>
        </div>

        <div className="row mb-3">
          <div className="col-sm-6">
            <label className="form-label">사업자 번호</label>
            <p className="form-control-plaintext">{business_register_number}</p>
          </div>
          <div className="col-sm-6">
            <label className="form-label">설명 (요구 정보)</label>
            <p className="form-control-plaintext">{business.description}</p>
          </div>
        </div>

        <div className="mb-4">
          <label className="form-label">편의 시설</label>
          <p className="form-control-plaintext">{business.facilities}</p>
        </div>

        {/* 선택 리스트들 */}
        {petList?.length > 0 ? (
          <>
            <div className="mt-2">
              <strong>선택된 펫:</strong>{' '}
              {selectedPet.length > 0
                ? selectedPet.map(p => (
                    <span key={p.id} className="badge bg-primary me-1">
                      {p.name+', '}
                    </span>
                  ))
                : <span className="text-muted">없음</span>
              }
            </div>
            <label className="form-label">애완동물 선택</label>
            <PetList pets={petList} selected={handlePetSelected} />
          </>
        ) : (
          <div className="alert alert-warning">회원님의 애완동물을 등록해주세요!</div>
        )}

        {roomList?.length > 0 && (
          <>
            <label className="form-label mt-4">방 선택</label>
            <BusinessRoomList roomList={roomList} onRoomSelect={handleRoomSelect} />
          </>
        )}

        {/* 날짜 및 요청사항 */}
        <div className="row g-3 my-4">
          <div className="col-md-6">
            <label htmlFor="checkIn" className="form-label">체크인 날짜</label>
            <input
              type="date"
              id="checkIn"
              name="checkIn"
              className="form-control"
              value={reservationForm.checkIn}
              onChange={handleChange}
            />
          </div>
          <div className="col-md-6">
            <label htmlFor="checkOut" className="form-label">체크아웃 날짜</label>
            <input
              type="date"
              id="checkOut"
              name="checkOut"
              className="form-control"
              value={reservationForm.checkOut}
              onChange={handleChange}
            />
          </div>
        </div>

        <div className="mb-3">
          <label htmlFor="specialRequests" className="form-label">특별 요구사항</label>
          <input
            type="text"
            id="specialRequests"
            name="specialRequests"
            className="form-control"
            value={reservationForm.specialRequests}
            onChange={handleChange}
          />
        </div>

        <div className="mb-4">
          <label htmlFor="businessRequestInfo" className="form-label">사업자 요구정보</label>
          <input
            type="text"
            id="businessRequestInfo"
            name="businessRequestInfo"
            className="form-control"
            value={reservationForm.businessRequestInfo}
            onChange={handleChange}
          />
        </div>

        <Button
          classtext="btn btn-success"
          type="button"
          onClick={() => handleSubmit}
          title="예약 하기"
        />
      </form>
    </div>
  );
}

export default ReservationForm;
