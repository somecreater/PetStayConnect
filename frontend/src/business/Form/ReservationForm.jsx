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
    const res= await ApiService.businessroom.list(business.id);
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

  const readonlyItems = [
    ['회원님 아이디', user_login_id],
    ['사업체 이름', business.businessName],
    ['사업자 번호', business_register_number],
    ['설명 (요구 정보)', business.description],
    ['편의 시설', business.facilities],
    ['인증 여부', business.varification]
  ];

  const inputItems = [
    { name: 'checkIn', label: '체크인 날짜', type: 'date' },
    { name: 'checkOut', label: '체크아웃 날짜', type: 'date' },
    { name: 'specialRequests', label: '특별 요구사항', type: 'text' },
    { name: 'businessRequestInfo', label: '사업자 요구정보', type: 'text' },
  ];

  //예약 폼
  return (
    <div className="container py-4">
      <form onSubmit={e => { e.preventDefault(); handleSubmit(); }}>

        <div
          style={{ maxHeight: '75vh', overflowY: 'auto' }}
          className="p-2 border rounded"
        >
        {/* 사업자 정보 */}
        <div className="col-12">
          <div 
            className="p-2 border rounded overflow-auto" 
          >
            <dl className="row g-1 m-0">
              {readonlyItems.map(([label, value]) => (
                <React.Fragment key={label}>
                  <dt className="col-4 fw-bold small text-truncate">{label}</dt>
                  <dd className="col-8 small mb-1">{value}</dd>
                </React.Fragment>
              ))}
            </dl>
          </div>
        </div>


        {/* 선택 리스트들 */}
        <div className="col-12">
          <div className="small text-secondary mb-1">애완동물 선택</div>
          <div className="small">
            선택된 펫:{' '}
            {selectedPet.length > 0
              ? selectedPet.map(p => (
                  <span key={p.id} className="badge bg-primary me-1">
                    {p.name+', '} 
                  </span>
                ))
              : <span className="text-muted">없음</span>
            }
          </div>
          <div
            className="p-2 border rounded overflow-auto mb-2"
          >
            {petList?.length > 0 
              ? <PetList pets={petList} selected={handlePetSelected} />
              : <div className="text-warning small">펫을 먼저 등록해주세요!</div>
            }
          </div>
        </div>

        {/* 방 선택 */}
        {roomList.length > 0 && (
          <div className="col-12">
            <div className="small text-secondary mb-1">방 선택</div>
            <BusinessRoomList roomList={roomList} onRoomSelect={handleRoomSelect} />

            {/* ✨ 선택된 방 표시 ✨ */}
            {selectedRoom && (
              <div className="mt-2 p-2 border rounded bg-light">
                <strong>선택된 방:</strong> {selectedRoom.roomType} – {selectedRoom.description}
              </div>
            )}
          </div>
        )}

        {/* 날짜 및 요청사항 */}
          <div className="row g-2 mb-3">
            {inputItems.map(({ name, label, type }) => (
              <div key={name} className="col-md-6">
                <label htmlFor={name} className="visually-hidden">{label}</label>
                <input
                  id={name}
                  name={name}
                  type={type}
                  className="form-control form-control-sm"
                  placeholder={label}
                  value={reservationForm[name]}
                  onChange={handleChange}
                />
              </div>
            ))}
          </div>
        </div>
        {/* 예약 버튼 */}
        <div className="col-12 text-end">
          <Button
            classtext="btn btn-success btn-sm"
            type="submit"
            title="예약하기"
          />
        </div>
      </form>
    </div>
  );
}

export default ReservationForm;
