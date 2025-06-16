import React, { useEffect, useState } from "react";
import ApiService from "../../common/Api/ApiService";
import { useUser } from "../../common/Context/UserContext";
import ReservationList from "../Component/ReservationList";
import BusinessRoomList from "../Component/BusinessRoomList";
import PayList from "../../pay/Component/PayList";
import Modal from "../../common/Ui/Modal";
import RoomRegisterForm from "../Form/RoomRegisterForm";
import RoomUpdateForm from "../Form/RoomUpdateForm";
import { useNavigate } from "react-router-dom";
import BusinessTagList from "../Component/BusinessTagList";
import Button from "../../common/Ui/Button";


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
  const navigate= useNavigate();
  const businessId = user.petBusinessDTO?.id;
  const business_name = user.petBusinessDTO?.businessName;

  const [activeTab, setActiveTab] = useState("reservations"); // "reservations" | "rooms" | "payments" | "tags"
  const [updateModal, setUpdateModal] = useState(false);
  const [registerModal, setRegisterModal] = useState(false);
  const [showTagForm,setShowTagForm] = useState(false);

  const [reservations,setReservations]= useState([]);
  const [rooms, setRooms] = useState([]);
  const [pays, setPays] = useState([]);
  const [tags, setTags] = useState([]);
  const [page, setPage] = useState(0);
  const [size] = useState(10);
  const [TotalPages, setTotalPages] = useState(1);

  const [reservation, setReservation] = useState(null);
  const [room, setRoom] = useState(null);
  const [pay, setPay] = useState(null);
  const [tag, setTag] = useState(null);

  const [tagName, setTagName] = useState("");
  const [tagType, setTagType] = useState("PET_SPECIES");

  const reservationList=async () =>{ 
    const response= await ApiService.business.bnsReservation(page, size);
    const data= response.data;

    if(data.result){
      console.log("예약 목록을 가져왔습니다.");
      setReservations(data.reservations);
      setPage(page);
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
    const response= await ApiService.payments.bnslist(businessId,page,size);
    const data= response.data;

    if(data.result){
      console.log("결제 목록을 가져왔습니다.");
      setPays(data.list);
      setTotalPages(data.totalPages);
      setPage(page);
    }else{
      console.log("결제 목록이 없거나 가져오지 못했습니다.");
      setPays([]);
    }
  };

  const tagList= async ()=>{
    const response= await ApiService.businesstag.list(businessId,page,size);
    const data= response.data;

    if(data.result){
      setTags(data.tags);
    }else{
      setTag([]);
    }
  }

  useEffect(() => {
  if (!businessId) {
    return;
  }
  if (activeTab === "reservations") {
    reservationList();
  }
  else if (activeTab === "rooms") {
    roomList();
  }
  else if (activeTab === "payments") {
    payList();
  }
  else if (activeTab === "tags") {
    tagList();
  }

  },[activeTab, businessId, page]);

  const goPrevPage = () => {
    if (page > 1){ 
      setPage(page-1);
    }
  };
  const goNextPage = () => {
    if (page < TotalPages){ 
      setPage(page+1);
    }
  };

  const ReservationDelete= async (id) => {
    const response= await ApiService.business.deleteReservation(id);
    const data=response.data;
    if(data.result){
      alert("예약이 취소되었습니다.");
      reservationList();
    }else{
      alert("이미 날짜가 지난 예약이거나, "+
        "시스템 문제로 실패하였습니다.");
    }
  };
  const PayDelete= async (dto) => {
    const id= dto.id;
    const request= {
      impUid: dto.impUid,
      merchantUid: dto.merchantUid,
      amount: dto.amount,
      reason: '환불처리'
    };
    const response= await ApiService.payments.delete(request, id);
    const data=response.data;
    if(data.result){
      alert("해당 결제를 취소 시켰으며, 해당되는 예약도 삭제되었습니다.");
      payList();
    }else{
      alert("이미 날짜가 지난 예약 이거나, " +
        " 잔액 부족 등의 문제로 결제가 취소되지 않았습니다!");
    }
  };
  const RoomDelete= async (id) => {
    const response= await ApiService.businessroom.delete(businessId,id);
    const data= response.data;
    if(data.result){
      alert("방이 삭제되었습니다.\n"+
        " 탭 전환이나 새로 고침시 데이터가 변경됩니다.");
      roomList();
    }else{
      alert("방 삭제에 실패했습니다!");
    }
  }
  const TagDelete= async (id) => {
    const response= await ApiService.businesstag.delete(id);
    const data= response.data;
    if(data.result){
      alert(data.message);
      tagList();
    }else{
      alert(data.message);
    }
  };

  const SelectedRoom = (selRoom)=>{
    if(room === null){
      setRoom(selRoom);
    }else{
      setRoom(null);
    }
  };
  const SelectReservation = (selReservation)=>{
    if(reservation === null){
      setReservation(selReservation);
    }else{
      setReservation(null);
    }
  };
  const SelectPay = (selPay) =>{
  if (pay?.id === selPay.id) {
    setPay(null);
  } else {
    setPay(selPay);
  }
  };
  const SelectTag = (selTag) =>{
    if(tag?.id ===selTag.id){
      setTag(null);
    }else{
      setTag(selTag);
    }
  };

  const tagSubmit = async ()=> {
    const dto={
      id: null,
      tagName: tagName,
      tagType: tagType,
      business_id: businessId,
      business_name: business_name
    }
    if(tagName  !== ""){
      const response= await ApiService.businesstag.register(dto);
      const data= response.data;

      if(data.result){
        tagList();
      }else{
        alert(data.message);
      }
    }
  };

  return (
    <div className="container py-4">
      <h2 className="mb-4">사업자 관리 페이지</h2>

      <button className="btn btn-primary" onClick={()=>navigate('/user/info')}>마이페이지로 돌아가기</button>
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
        <li className="nav-item">
          <button
            className={`nav-link ${activeTab === "tags" ? "active" : ""}`}
            onClick={() => setActiveTab("tags")}
          >
            태그 관리
          </button>
        </li>
      </ul>

      {activeTab === "reservations" &&(
        <div>
          {/*예약 리스트 내 수정, 삭제(거부) 포함 */}
          <h5>예약 목록</h5>
          <ReservationList 
            List={reservations}
            isDelete={true}
            onDelete={ReservationDelete}
            isUpdate={false}
            onUpdate={null}
            onSelect={SelectReservation}
            isBusiness={true}
          />

          {/* 선택된 예약 표시 */}
          {reservation && (
            <div className="mt-3 p-2 border rounded bg-light">
              <strong>선택된 예약:</strong> #{reservation.id} ·{" "}
              {reservation.checkIn} ~ {reservation.checkOut}
            </div>
          )}
          
          
          <div className="d-flex justify-content-center align-items-center my-3">
            <button
              className="btn btn-outline-secondary btn-sm mx-2"
              onClick={goPrevPage}
              disabled={page+1 === 1}
            >
              이전
            </button>
            <span>
              {page+1} / {TotalPages}
            </span>
            <button
              className="btn btn-outline-secondary btn-sm mx-2"
              onClick={goNextPage}
              disabled={page+1 === TotalPages}
            >
              다음
            </button>
          </div>
        </div>
      )}
      {activeTab === "rooms" &&(
        <div>
          <button className="btn btn-primary btn-sm" onClick={()=>setRegisterModal(true)}>새 룸 추가</button>
          <button className="btn btn-primary btn-sm" onClick={()=>setUpdateModal(true)}>룸 수정</button>
          <div className="d-flex justify-content-between align-items-center mb-2">
            <h5>룸 목록</h5>

            {room &&(
              <div className="mt-2 p-2 border rounded bg-light">
                <strong>선택된 방:</strong> {room.roomType} · {room.description}
              </div>
            )}
          </div>
          
          <div className="mt-3">
            <BusinessRoomList
              roomList={rooms}
              onRoomSelect={SelectedRoom}
              isDelete={true}
              onDelete={RoomDelete}
            />
          </div>
          
          {(registerModal && activeTab === "rooms")&&
            <Modal isOpen={registerModal} onClose={()=>{setRegisterModal(false); roomList();}}>
              <RoomRegisterForm/>
            </Modal>
          }
          {(updateModal && room && activeTab === "rooms")&&
            <Modal isOpen={updateModal} onClose={()=>{setUpdateModal(false); roomList();}}>
              <RoomUpdateForm room={room}/>
            </Modal>
          }
        </div>
      )}
      {activeTab === "payments" &&(
        <div>
          <h5>결제 목록</h5>

          {pay && (
            <div className="mt-3 p-2 border rounded bg-light">
              <strong>선택된 결제:</strong> #{pay.id} · 금액: {pay.amount}원
            </div>
          )}

          <PayList
            List={pays}
            isBusiness={true}
            isDelete={true}
            onDelete={PayDelete}
            onSelected={SelectPay}
          />

          <div className="d-flex justify-content-center align-items-center my-3">
            <button
              className="btn btn-outline-secondary btn-sm mx-2"
              onClick={goPrevPage}
              disabled={page+1 === 1}
            >
              이전
            </button>
            <span>
              {page+1} / {TotalPages}
            </span>
            <button
              className="btn btn-outline-secondary btn-sm mx-2"
              onClick={goNextPage}
              disabled={page+1 === TotalPages}
            >
              다음
            </button>
          </div>
        </div>
      )}
      {activeTab === "tags" &&(
        <div>
          <h5>태그 목록</h5>
          <Button 
            classtext="btn btn-primary btn-sm" 
            type="button" 
            title={showTagForm ? '태그 등록 폼 숨기기':'태그 등록 폼'}
            onClick={()=>setShowTagForm(prev => !prev)} 
          />

          {showTagForm &&(
            <div className="mb-4">
              <form onSubmit={tagSubmit} className="border p-4 rounded bg-light">
                <h5 className="mb-4">신규 태그 등록</h5>
                
                <div className="mb-3">
                  <label className="form-label">태그 종류</label>
                  <select
                    className="form-select"
                    value={tagType}
                    onChange={e => setTagType(e.target.value)}
                  >
                    <option value="PET_SPECIES">애완동물 종</option>
                    <option value="PET_WEIGHT">애완동물 사이즈</option>
                  </select>
                </div>
                
                <div className="mb-3">
                  <label className="form-label">태그 이름</label>
                  <input
                    className="form-control"
                    type="text"
                    value={tagName}
                    onChange={e => setTagName(e.target.value)}
                  />
                </div>

                <Button
                  classtext="btn btn-success w-100" 
                  type="submit" 
                  title="태그 등록" 
                />
              </form>
            </div>
          )}

          {tag &&(
            <div className="mt-3 p-2 border rounded bg-light">
              <strong>선택된 태그:</strong> #{tag.tagType} · 이름: {tag.tagName}
            </div>
          )}

          <BusinessTagList
            tagList={tags}
            onSelect={SelectTag}
            isDelete={true}
            onDelete={TagDelete}
          />
        </div>
      )}
    </div>
  );
}

export default BusinessManagePage;
