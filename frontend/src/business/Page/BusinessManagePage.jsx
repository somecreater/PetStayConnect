import React, { useState } from "react";
import ApiService from "../../common/Api/ApiService";
import { useUser } from "../../common/Context/UserContext";

//예약 관리, 룸 관리, 결제 관리 등을 포함
function BusinessManagePage(props){
  const { user } = useUser();
  const isProvider= user.role === 'SERVICE_PROVIDER';
  const [reservations,setReservations]= useState([]);
  const [rooms, setRooms]= useState([]);

  const reservationList=async () =>{ 
    const response= await ApiService.business.bnsReservation();
    const data= response.data;

    if(data.result){
      console.log("예약 목록을 가져왔습니다.");
      setReservations(data.reservationList);
    }else{
      console.log("예약 목록이 없거나 가져오지 못했습니다.");
      setReservations([]);
    }
  };

  const roomList= async () => {
    const response= await ApiService.businessroom.list(user.petBusinessDTO.id);
    const data= response.data;

    if(data.result){
      console.log("룸 목록을 가져왔습니다.");
      setRooms(data.rooms);
    }else{
      console.log("룸 목록이 없거나 가져오지 못했습니다.");
      setRooms([]);
    }
  };

  const payList= async ()=>{
    
  };

  return (
    <div>
    
    </div>
  );
}

export default BusinessManagePage;