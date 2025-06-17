import React, { useCallback, useContext, useEffect, useState } from 'react';
import BusinessRoomList from '../Component/BusinessRoomList';
import PetList from '../../pet/Component/PetList';
import ApiService from '../../common/Api/ApiService';
import PaymentForm from '../../pay/Form/PaymentForm';
import Button from '../../common/Ui/Button';
import Modal from '../../common/Ui/Modal';

function ReservationForm(props){

  const { user_login_id, business_register_number, petList, business } = props;
  const [selectedRoom, setSelectedRoom] = useState(null);
  const [businessName,setBusinessName]= useState('');
  const [price, setPrice] = useState(0);
  const [newReservation, setNewReservation]= useState(null);
  const [paymentModal,setPaymentModal]= useState(false);
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
    if(selectedRoom?.id === room.id){
      setSelectedRoom(null);
      setReservationForm(prev => ({ ...prev, roomType: ''}));
    }
    else{
      setSelectedRoom(room);
      setReservationForm(prev => ({ ...prev, roomType: room.roomType }));
    }
  };

  const handleSubmit = async ()=>{
    try{
      const response = await ApiService.business.reservation(reservationForm,business.id);
      const data=response.data;

      if(data.result){
        alert(data.message);
        console.log(data.reservation);
        setPaymentModal(true);
        setBusinessName(data.business_name);
        setNewReservation(data.reservation);
        setPrice(data.price);
      }else{
        alert(data.message);
      }
    }catch(err){
      console.error(err);
      alert("예약 등록중 오류발생!!");
    }
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
          className="p-3 border rounded bg-light"
        >
        {/* 사업자 정보 */}
        <section className="mb-4">
          <h6 className="fw-bold border-bottom pb-1 mb-2">사업자 정보</h6>
            <dl className="row g-1">
              {readonlyItems.map(([label, value]) => (
                <React.Fragment key={label}>
                  <dt className="col-4 fw-bold small text-truncate">{label}</dt>
                  <dd className="col-8 small mb-1">{value}</dd>
                </React.Fragment>
              ))}
            </dl>
        </section>


        {/* 선택 리스트들 */}
        <section className="mb-4">
          <h6 className="fw-bold border-bottom pb-1 mb-2">애완동물 선택</h6>
          <div className="mb-2">
            {selectedPet.length > 0 ? (
              selectedPet.map(p => (
                <span key={p.id} className="badge bg-info text-dark me-1">{p.name}</span>
              ))
            ) : (
              <span className="text-muted small">선택된 펫이 없습니다.</span>
            )}
          </div>
          <div className="p-2 border rounded bg-beige-yellow">
            {petList?.length > 0 ? (
              <PetList pets={petList} selected={handlePetSelected} />
            ) : (
              <div className="text-warning small">펫을 먼저 등록해주세요!</div>
            )}
          </div>
        </section>

        {/* 방 선택 */}
        {roomList.length > 0 && (
        <section className="mb-4">
          <h6 className="fw-bold border-bottom pb-1 mb-2">방 선택</h6>
          <BusinessRoomList roomList={roomList} onRoomSelect={handleRoomSelect} />
          {selectedRoom && (
            <div className="mt-2 p-2 bg-white border rounded">
              <span className="fw-semibold">선택된 방:</span> {selectedRoom.roomType} – {selectedRoom.description}
            </div>
          )}
        </section>
        )}

        {/* 날짜 및 요청사항 */}
        <section className="mb-4">
          <h6 className="fw-bold border-bottom pb-1 mb-2">예약 정보 입력</h6>
          <div className="row g-2">
            {inputItems.map(({ name, label, type }) => (
              <div key={name} className="col-md-6">
                <label htmlFor={name} className="form-label small fw-semibold">{label}</label>
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
        </section>
        {/* 예약 버튼 */}
        <div className="text-end">
          <Button
            classtext="btn btn-success btn-sm px-4"
            type="submit"
            title="예약하기"
          />
        </div>
        </div>
      </form>
      {paymentModal && newReservation &&
        <Modal isOpen={paymentModal} onClose={() => setPaymentModal(false)}>
          <PaymentForm reservation={newReservation} price={price} businessName={businessName}/>
        </Modal>
      }
    </div>
  );
}

export default ReservationForm;
