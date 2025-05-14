import React, { useState } from "react";
import { useUser } from "../../common/Context/UserContext";
import ApiService from "../../common/Api/ApiService";

function RoomUpdateForm(props){
  
  const {user} = useUser();
  const {room} = props;
  const [updateRoom, setUpdateRoom]=useState({
    id: room.id,
    petBusinessId: room.petBusinessId,
    petBusinessRegisterNumber: room.petBusinessRegisterNumber,
    roomType: room.roomType,
    description: room.description,
    roomCount: room.roomCount,
    reservationDTOList: null
  });

  const handleChange = (e) => {
    const {name, value} = e.target;
    setUpdateRoom(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit= async (e) => {
    e.preventDefault();
    try{
      const response= await ApiService.businessroom.update(user.petBusinessDTO.id, room.id, updateRoom);
      const data=response.data;
      if(data.result){
        alert(data.message);
      }else{
        alert(data.message);
      }
    }catch(err){
      console.log(err);
      alert("방을 수정하는 것에 실패하였습니다.");
    }
  }
  return (
    <div className="container py-4">
      <form onSubmit={handleSubmit} className="border p-4 rounded bg-light">
        <h5 className="mb-3">#{room.id} 방 수정</h5>
        
        <div className="mb-3 row">
          <label className="col-sm-3 col-form-label">ID</label>
          <div className="col-sm-9">
            <p className="form-control-plaintext">
              {room.id}
            </p>
          </div>
        </div>

        
        <div className="mb-3">
          <label className="col-sm-3 col-form-label">타입</label>
          <input 
            type="text"
            className="form-control"
            name="roomType" 
            id="roomType"
            value={updateRoom.roomType}
            onChange={handleChange}
            placeholder="타입"
          />
        </div>

        <div className="mb-3">
          <label className="col-sm-3 col-form-label">설명</label>
          <input 
            type="text"
            className="form-control"
            name="description"  
            id="description"
            value={updateRoom.description}
            onChange={handleChange}
            placeholder="설명"
          />
        </div>

        <div className="mb-3">
          <label className="col-sm-3 col-form-label">방 수</label>
          <input 
            type="number"
            className="form-control"
            name="roomCount"  
            id="roomCount"
            value={updateRoom.roomCount}
            onChange={handleChange}
          />
        </div>
        <div className="text-end">
          <button type="submit" className="btn btn-primary">
            수정 완료
          </button>
        </div>
      </form>
    </div>
  );
}

export default RoomUpdateForm;
