import React from "react";
import Reservation from "./Reservation";
import Button from '../../common/Ui/Button';

function ReservationList({List, isDelete, onDelete, isUpdate, onUpdate, onSelect, isBusiness}){
  if (!List || List.length === 0) {
    return <div className="text-center text-muted">예약 내역이 없습니다.</div>;
  }
  const handleClick= (reservation)=>{
    onSelect(reservation);
  }
  return (
    <div className="row gy-4">
      {List.map(reservation => (
        <div 
          key={reservation.id} 
          className="col-12" 
          onClick={()=>handleClick(reservation)}
        >
          <Reservation reservationDTO={reservation} />

          {/* 액션 버튼 영역 */}
          <div className="d-flex justify-content-end gap-2">
            {isUpdate && typeof onUpdate === 'function' && (
              <Button
                classtext="btn btn-outline-primary btn-sm"
                type="button"
                title="수정"
                onClick={() => onUpdate(reservation, reservation.id)}
              />
            )}
            {!isBusiness && isDelete && typeof onDelete === 'function' && (
              <Button
                classtext="btn btn-outline-danger btn-sm"
                type="button"
                title="삭제"
                onClick={() => onDelete(reservation.id)}
              />
            )}
            {isBusiness && (
              <>
                <Button
                  classtext="btn btn-warning btn-sm"
                  type="button"
                  title="거절"
                  onClick={() => onDelete(reservation.id)}
                />
              </>
            )}
          </div>
        </div>
      ))}
    </div>
  );
}

export default ReservationList;
