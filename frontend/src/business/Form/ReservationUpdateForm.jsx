import React, { useState, useEffect } from 'react';
import PetList from '../../pet/Component/PetList';
import BusinessRoomList from '../Component/BusinessRoomList';
import ApiService from '../../common/Api/ApiService';
import Button from '../../common/Ui/Button';

function ReservationUpdateForm({ reservation, onSubmit, onCancel }){
  const [formData, setFormData] = useState({
    id: reservation.id,
    petBusinessId: reservation.petBusinessId,
    checkIn: reservation.checkIn,
    checkOut: reservation.checkOut,
    specialRequests: reservation.specialRequests || '',
    businessRequestInfo: reservation.businessRequestInfo || '',
    petDTOList: reservation.petDTOList || [],
    roomType: reservation.roomType,
    petBusinessRoomId: reservation.petBusinessRoomId
  });
  
  //추후 로직 수정(예약된 펫 정보, 방 가져오도록)
  const [pets, setPets] = useState([]);
  const [rooms, setRooms] = useState([]);

  //사용자의 애완동물 목록, 사업자의 방목록을 가져온다.
  useEffect(() => {
    ApiService.pet.userpet()
      .then(res => res.data.result && setPets(res.data.pets))
      .catch(console.error);

    ApiService.businessroom.list(reservation.petBusinessId)
      .then(res => res.data.result && setRooms(res.data.rooms))
      .catch(console.error);

    ApiService.pet.reservation(reservation.id)
      .then(res => {
        if (res.data.result) {
          setFormData(prev => ({ ...prev, petDTOList: res.data.pets }));
        }
      })
      .catch(console.error);

  }, [reservation.id, reservation.petBusinessId]);

  const handleChange = e => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handlePetSelect = pet => {
    const exists = formData.petDTOList.find(p => p.id === pet.id);
    const updated = exists
      ? formData.petDTOList.filter(p => p.id !== pet.id)
      : [...formData.petDTOList, pet];
    setFormData(prev => ({ ...prev, petDTOList: updated }));
  };

  const handleRoomSelect = room => {
    setFormData(prev => {
      if (prev.petBusinessRoomId === room.id) {
        return { ...prev, roomType: '', petBusinessRoomId: null };
      }
      return { ...prev, roomType: room.roomType, petBusinessRoomId: room.id };
    });
  };


  const submit = e => {
    e.preventDefault();
    onSubmit(formData, reservation.id);
  };

  const selectedRoomObj = rooms.find(r => r.id === formData.petBusinessRoomId);

  return (
    <form onSubmit={submit} className="p-3">
      <h5 className="mb-3">예약 정보 수정</h5>


      <div className="mb-3">
        <strong>선택된 펫:</strong>{' '}
        {formData.petDTOList.length > 0 ? (
          formData.petDTOList.map(p => (
            <span key={p.id} className="badge bg-secondary me-1">
              {p.name}
            </span>
          ))
        ) : (
          <span className="text-muted">없음</span>
        )}
      </div>
      <div className="mb-2 text-muted small">
        ※ 이미 등록된 애완동물은 수정할 수 없습니다. 해당 예약을 취소하고 다시 예약 해주세요.
      </div>
      <div className="mb-3 p-2 border rounded overflow-auto">
        <PetList
          pets={pets}
          isDelete={false}
          isEdit={false}
          onDelete={null}
          onEdit={null}
          selected={handlePetSelect}
        />
      </div>
      
      <div className="mb-3">
        <strong>선택된 방:</strong>
        {selectedRoomObj ? (
          <span className="badge bg-secondary">
            {selectedRoomObj.roomType} – {selectedRoomObj.description}
          </span>
        ) : (
          <span className="text-muted">없음</span>
        )}
      </div>

      <div className="mb-3 p-2 border rounded overflow-auto">
        <BusinessRoomList
          roomList={rooms}
          onRoomSelect={handleRoomSelect}
          isDelete={false}
        />
      </div>

      <div className="row g-2 mb-3">
        <div className="col-md-6">
          <input
            type="date"
            name="checkIn"
            value={formData.checkIn}
            onChange={handleChange}
            className="form-control form-control-sm"
          />
        </div>
        <div className="col-md-6">
          <input
            type="date"
            name="checkOut"
            value={formData.checkOut}
            onChange={handleChange}
            className="form-control form-control-sm"
          />
        </div>
      </div>

      <div className="mb-3">
        <textarea
          name="specialRequests"
          placeholder="특별 요청사항"
          value={formData.specialRequests}
          onChange={handleChange}
          className="form-control form-control-sm"
          rows={2}
        />
      </div>
      <div className="mb-3">
        <textarea
          name="businessRequestInfo"
          placeholder="사업자 요청정보"
          value={formData.businessRequestInfo}
          onChange={handleChange}
          className="form-control form-control-sm"
          rows={2}
        />
      </div>

      <div className="d-flex justify-content-end">
        <Button type="button" title="취소" classtext="btn btn-secondary btn-sm me-2" onClick={onCancel} />
        <Button type="button" title="수정 완료" classtext="btn btn-primary btn-sm" onClick={submit}/>
      </div>
    </form>
  );
}

export default ReservationUpdateForm;
