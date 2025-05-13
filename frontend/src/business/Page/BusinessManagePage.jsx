import React, { useEffect, useState } from "react";
import ApiService from "../../common/Api/ApiService";
import { useUser } from "../../common/Context/UserContext";
import ReservationList from "../Component/ReservationList";
import BusinessRoomList from "../Component/BusinessRoomList";
import PayList from "../../pay/Component/PayList";


/*
예약 관리, 룸 관리, 결제 관리 등을 포함
각각 crud를 포함하되 일부 조건에 따라 제한,
조건 관련(이미 지난 예약 여부, 예약이 포함된 방 여부)은 백에서 처리
예약(rd만 가능), 
룸(crud 가능), 
결제(r만 가능)
*/
function BusinessManagePage(props){
  const { user } = useUser();
  const businessId = user.petBusinessDTO?.id;

  const [activeTab, setActiveTab] = useState("reservations"); // "reservations" | "rooms" | "payments"
  const [reservations,setReservations]= useState([]);
  const [rooms, setRooms]= useState([]);
  const [pays, setPays]= useState([]);
  const [room, setRoom]= useState(null);

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
    const response= await ApiService.businessroom.list(businessId);
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
    const response= await ApiService.payments.list();
    const data= response.data;

    if(data.result){
      console.log("결제 목록을 가져왔습니다.");
      setPays(data.pays);
    }else{
      console.log("결제 목록이 없거나 가져오지 못했습니다.");
      setPays([]);
    }
  };

  useEffect(() => {
    //reservationList();
    //roomList();
    //payList();
  },[businessId]);

  const ReservationDelete= async (id) => {
    const response= await ApiService.business.deleteReservation(id);
    const data=response.data;
    if(data.result){
      alert("예약이 취소되었습니다.");
      
    }else{
      alert("이미 날짜가 지난 예약이거나, "+
        "시스템 문제로 실패하였습니다.");
    }
  };

  const SelectedRoom = (room)=>{
    setRoom(room);
  }

  return (
    <div className="container py-4">
      <h2 className="mb-4">사업자 관리 페이지</h2>
      <ul className="nav nav-tabs mb-3">
        <li className="nav-item">
          <button
            className={`nav-link ${activeTab === "reservations" ? "active" : ""}`}
            onClick={() => setActiveTab("reservations")}
          >
            예약 관리
          </button>
        </li>
        <li className="nav-item">
          <button
            className={`nav-link ${activeTab === "rooms" ? "active" : ""}`}
            onClick={() => setActiveTab("rooms")}
          >
            룸 관리
          </button>
        </li>
        <li className="nav-item">
          <button
            className={`nav-link ${activeTab === "payments" ? "active" : ""}`}
            onClick={() => setActiveTab("payments")}
          >
            결제 관리
          </button>
        </li>
      </ul>

      {activeTab === "reservations" &&(
        <div>
          <h5>예약 목록</h5>
          <ReservationList 
            List={reservationList}
            isDelete={true}
            onDelete={ReservationDelete}
            isUpdate={false}
            onUpdate={null}
            isBusiness={true}
          />
        </div>
      )}
      {activeTab === "rooms" &&(
        <div>
          <div className="d-flex justify-content-between align-items-center mb-2">
            <h5>룸 목록</h5>
            <button className="btn btn-primary btn-sm">새 룸 추가</button>
          </div>
          <BusinessRoomList
            roomList={rooms}
            onRoomSelect={SelectedRoom}
          />

          
        </div>
      )}
      {activeTab === "payments" &&(
        <div>

        </div>
      )}
    </div>
  );
}

export default BusinessManagePage;