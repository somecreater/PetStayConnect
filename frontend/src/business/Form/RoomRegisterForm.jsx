import React, { useState } from "react";
import { useUser } from "../../common/Context/UserContext";
import ApiService from "../../common/Api/ApiService";

function RoomRegisterForm(props){

  const {user, updateUser} =useUser();
  const [newroom, setNewroom]=useState({
    id: null,
    petBusinessId: user.petBusinessDTO.id,
    petBusinessRegisterNumber: user.petBusinessDTO.registrationNumber,
    roomType: '',
    description: '',
    roomCount: 0,
    reservationDTOList: null
  });

  const handleChange = (e) => {
    const {name, value} = e.target;
    setNewroom(prev => ({ ...prev, [name]: value }));
  }

  const handleSubmit = async (e)=> {
    e.preventDefault();

    try{
      const response = await ApiService.businessroom.register(newroom.petBusinessId,newroom);
      const data= response.data;

      if(data.result){
        const room=data.room;
        console.log(data.message);
        alert("새로운 방이 추가되었습니다.\n" 
          + "새로운 방 타입: " + room.roomType 
          + "새로운 방 설명: " + room.description
          + "새로운 방의 수: " + room.roomCount);
      }else{
        const message=data.message;
        alert(message);
      }

    }catch(err){
      console.err(err);
      alert('새로운 방 등록에 실패했습니다. 다시 시도해보세요');
    }

  }

  return (
    <div className="container py-4">
      <form onSubmit={handleSubmit} className="border p-4 rounded bg-light">
        <h4 className="mb-4">새로운 방 등록</h4>

        <div className="mb-3">
          <label className="form-label">타입</label>
          <input
            className="form-control"
            type="text"
            name="roomType"
            value={newroom.roomType}
            placeholder="방 타입"
            onChange={handleChange}
          />
        </div>

        <div className="row g-3">   
          <div className="col-md-6">
            <label className="form-label">설명</label>
            <input
              className="form-control"
              type="text"
              name="description"
              value={newroom.description}
              placeholder="방 설명"
              onChange={handleChange}
          />
          </div>
          
          <div className="col-md-6">
            <label className="form-label">방 수</label>
            <input
              className="form-control"
              type="number"
              name="roomCount"
              value={newroom.roomCount}
              onChange={handleChange}
            />
          </div>
        </div>
        <button 
          className="btn btn-primary"
          type="submit"
        >
          추가하기
        </button>
      </form>
    </div>
  );
}

export default RoomRegisterForm;