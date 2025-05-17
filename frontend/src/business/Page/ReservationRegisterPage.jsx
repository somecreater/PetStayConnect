import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useUser } from '../../common/Context/UserContext';
import ApiService from '../../common/Api/ApiService';
import ReservationForm from '../form/ReservationForm';

export default function ReservationRegisterPage() {
  const { providerId } = useParams();
  const navigate = useNavigate();
  const { user } = useUser();
  const [business, setBusiness] = useState(null);
  const [petList, setPetList] = useState([]);

  useEffect(() => {
    const load = async () => {
      try {
        const petRes = await ApiService.pet.userpet();
        console.log('petRes ▶', petRes.data);
        setPetList(petRes.data.pets);

        const bizRes = await ApiService.business.list({ id: providerId }, 0, 1);
        console.log('bizRes ▶', bizRes.data);
        const items = bizRes.data.search?.content || bizRes.data.content || bizRes.data;
        if (!items || items.length === 0) throw new Error('No business');
        console.log('▶ 실제 business 객체:', items[0]);
        setBusiness(items[0]);
      } catch (err) {
        console.error('load error ▶', err);
        alert('업체 및 펫 데이터 로드 실패');
      }
    };
    load();
  }, [providerId]);

  if (!business) {
    return <div className="container mt-5">불러오는 중...</div>;
  }

  const handleSubmit = async formData => {
    try {
      const res = await ApiService.business.reservation(formData, business.id);
      if (res.data.result) {
        alert('예약이 완료되었습니다.');
        navigate('/reservations/user');
      } else {
        alert(res.data.message);
      }
    } catch {
      alert('예약 등록 중 오류가 발생했습니다.');
    }
  };

  return (
    <div className="container mt-5">
      <h2 className="mb-4">예약 등록</h2>
      <ReservationForm
        user_login_id={user.userLoginId}
        business_register_number={business.registrationNumber}
        business={business}
        petList={petList}
        submitText="예약하기"
        onSubmit={handleSubmit}
      />
    </div>
  );
}


