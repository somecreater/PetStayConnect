import React, { useEffect, useState } from 'react';
import { useUser } from '../../common/Context/UserContext';
import ApiService from '../../common/Api/ApiService';
import ReservationList from '../Component/ReservationList';
import Modal from '../../common/Ui/Modal';
import { useNavigate } from 'react-router-dom';
import ReservationUpdateForm from '../Form/ReservationUpdateForm';

function UserReservationManagePage(props){
  
  const navigate = useNavigate();
  const [reservation, setReservation]=useState(null);
  const [reservations, setReservations]=useState([]);
  const [updateModal,setUpdateModal]=useState(false);
  const [page, setPage]=useState(0);
  const [size, setSize]=useState(5);
  const {user}=useUser();

  const list = async () => {
    try {
      const response = await ApiService.business.conReservation(page, size);
      const data = response.data;
      if (data.result) {
        setReservations(data.reservations);
      } else {
        setReservations([]);
      }
    } catch (error) {
      console.error('예약 목록 조회 실패', error);
      setReservations([]);
    }
  };

  const handleDelete = (id) => {
    if (window.confirm('이 예약을 정말 삭제하시겠습니까?')) {
      remove(id);
    }
  };

  const remove = async (id) => {
    try {
      const response = await ApiService.business.deleteReservation(id);
      const data = response.data;
      if (data.result) {
        alert(data.message);
        list();
      } else {
        alert(data.message);
      }
    } catch (error) {
      console.error('예약 삭제 실패', error);
      alert('예약 삭제 중 오류가 발생했습니다.');
    }
  };

  const update = async (updatedData, id) => {
    try {
      const response = await ApiService.business.updateReservation(updatedData, id);
      const data = response.data;
      if (data.result) {
        alert(`${data.reservation.id}번 예약이 수정되었습니다.`);
        list();
      } else {
        alert(data.message);
      }
    } catch (error) {
      console.error('예약 수정 실패', error);
      alert('예약 수정 중 오류가 발생했습니다.');
    }
  };

  const selectReservation = (item) => {
    setReservation((prev) => (prev && prev.id === item.id ? null : item));
  };

  useEffect(()=>{
    list();
  },[size, page]);

  return (
    <div className="container py-4">
      <h2 className="mb-4">내 예약 관리</h2>
      <button className="btn btn-secondary mb-3" onClick={() => navigate('/user/info')}>
        마이페이지로 돌아가기
      </button>

      <div>
          <ReservationList 
            List={reservations}
            isDelete={true}
            onDelete={handleDelete}
            isUpdate={true}
            onUpdate={() => setUpdateModal(true)}
            onSelect={selectReservation}
            isBusiness={false}
          />

          {reservation && (
          <div className="mt-3 p-3 border rounded bg-light">
            <strong>선택된 예약:</strong> #{reservation.id} &nbsp;
            {reservation.checkIn} &ndash; {reservation.checkOut}
          </div>
          )}

        {updateModal && reservation && (
          <Modal isOpen={updateModal} onClose={() => setUpdateModal(false)}>
            <ReservationUpdateForm 
              reservation={reservation} 
              onSubmit={update}
              onCancel={()=> { setUpdateModal(false) }}/>
          </Modal>
        )}
        <div className="d-flex justify-content-between align-items-center mt-4">
          <button
            className="btn btn-outline-primary"
            onClick={() => setPage((p) => Math.max(p - 1, 0))}
            disabled={page === 0}
          >
            이전
          </button>
          <span>페이지: {page + 1}</span>
          <button
            className="btn btn-outline-primary"
            onClick={() => setPage((p) => p + 1)}
          >
            다음
          </button>

          <select
            className="form-select w-auto"
            value={size}
            onChange={(e) => setSize(Number(e.target.value))}
          >
            {[5, 10, 20].map((s) => (
              <option key={s} value={s}>{s}개씩 보기</option>
            ))}
          </select>
        </div>
      </div>
    </div>
  );
}

export default UserReservationManagePage;
