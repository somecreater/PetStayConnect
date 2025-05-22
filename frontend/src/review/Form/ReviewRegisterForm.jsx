import React, { useState, useEffect } from 'react';
import StarRating from '../Component/StarRating';
import { useNavigate } from 'react-router-dom';
import ApiService from '../../common/Api/ApiService';
import TextInput from '../../common/Ui/TextInput';
import Button from '../../common/Ui/Button';


export default function ReviewRegisterForm() {

  const [error, setError]           = useState(null);
  const [reservations, setReservations] = useState([]);
  const [loadingRes,  setLoadingRes]  = useState(true);
  const [fetchError,  setFetchError]  = useState(null);
  const [form, setForm] = useState({ reservationId: '', rating: 0, content: '' });
  const navigate = useNavigate();


  useEffect(() => {
    async function loadReservations() {
      try {
        const res = await ApiService.business.conReservation(0,100);
              console.log('GET /reservations 응답:', res.data);

        const list = res.data.reservations || [];
        setReservations(list.filter(r => r.status === 'COMPLETED'));
      } catch {
        setFetchError('예약 목록을 불러오지 못했습니다.');
      } finally {
        setLoadingRes(false);
      }
    }
    loadReservations();
  }, []);

  const handleChange = e => {
    const { name, value } = e.target;
    setForm(f => ({ ...f, [name]: value }));
   };
   const handleRatingChange = val => {
     setForm(f => ({ ...f, rating: val }));
   };

  const handleSubmit = async e => {
    e.preventDefault();
    setError(null);
    try {
      await ApiService.reviews.register({
        reservationId: Number(form.reservationId),
        rating: Number(form.rating),
        content: form.content,
      });
      navigate('/reviews');
    } catch (err) {
      setError(err.response?.data?.message || '리뷰 등록 중 오류가 발생했습니다.');
    }
  };

  return (
    <div className="container py-4">
      <form onSubmit={handleSubmit} className="card p-4">
        <h5 className="card-title mb-3">리뷰 등록</h5>

        {error && <div className="alert alert-danger">{error}</div>}

        <div className="mb-3">
          <label className="form-label">예약 선택</label>
          <select
            name="reservationId"
            className="form-select"
            value={form.reservationId}
            onChange={handleChange}
            required
          >

        <option value="">— 완료된 예약을 선택하세요 —</option>
        {reservations.map(r => (
          <option key={r.id} value={r.id}>
            {`${r.petBusinessName} @ ${r.roomType} (${r.checkIn}→${r.checkOut})`}
          </option>
        ))}
      </select>
    </div>

    <div className="col-md-3">
      <label className="form-label">평점</label>
      <StarRating rating={form.rating} onChange={handleRatingChange} />
    </div>

    <div className="mb-3">
      <label htmlFor="content" className="form-label">리뷰 내용</label>
      <textarea
        id="content"
        name="content"
        className="form-control"
        value={form.content}
        onChange={handleChange}
        rows="4"
        placeholder="리뷰를 입력하세요"
      />
    </div>

    <div className="d-grid">
      <Button type="submit" title="등록" classtext="btn btn-primary" />
     </div>
    </form>
   </div>
  );
}
