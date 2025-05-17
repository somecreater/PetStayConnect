import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useUser } from '../../common/Context/UserContext';
import ApiService from '../../common/Api/ApiService';
import ReservationForm from '../form/ReservationForm';

export default function ReservationUpdatePage() {
  const { providerId, reservationId } = useParams();
  const navigate = useNavigate();
  const { user } = useUser();
  const [business, setBusiness] = useState(null);
  const [petList, setPetList] = useState([]);
  const [initialData, setInitialData] = useState(null);


  useEffect(() => {
    if (initialData !== null) return;

    const loadData = async () => {
      try {
        const [petRes, bizRes, resRes] = await Promise.all([
          ApiService.pet.userpet(),
          ApiService.business.list({ id: providerId },0,1),
          ApiService.business.detailReservation(reservationId),
          ]);
          console.log('Loaded reservation detail ▶', resRes.data);


        setPetList(petRes.data.pets);
        setBusiness(bizRes.data.search.content[0]);
        setInitialData(resRes.data.reservation);
      } catch {
        alert('데이터 로드 실패');
      }
    }
    loadData();
  }, [providerId, reservationId]);

  if (!business) {
    return <div className="container mt-5">불러오는 중...</div>;
  }

  const handleUpdate = async formData => {
      const res = await ApiService.business.updateReservation(reservationId, formData);
      if (res.data.result) {
        alert('예약이 수정되었습니다.');
        navigate('/reservations/user');
      } else {
        alert(res.data.message);
      }
    };

    return (
      <div className="container mt-5">
        <h2 className="mb-4">예약 수정</h2>
        <ReservationForm
          reservationId={reservationId}
          user_login_id={user.userLoginId}
          business_register_number={business.registrationNumber}
          business={business}
          petList={petList}
          checkIn={initialData.checkIn}
          checkOut={initialData.checkOut}
          roomType={initialData.roomType}
          specialRequests={initialData.specialRequests}
          businessRequestInfo={initialData.businessRequestInfo}
          petDTOList={initialData.petDTOList}
          submitText="수정하기"
          onSubmit={handleUpdate}
        />
      </div>
    );
  }