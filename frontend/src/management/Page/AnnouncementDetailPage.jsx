import React, { useEffect, useState } from "react";
import { useUser } from "../../common/Context/UserContext";
import Announcement from "../component/Announcement";
import Modal from "../../common/Ui/Modal";
import AnnouncementUpdateForm from "../Form/AnnouncementUpdateForm";
import ApiService from "../../common/Api/ApiService";
import { useNavigate, useParams } from "react-router-dom";

function AnnouncementDetailPage(props){
  
  const navigate= useNavigate();
  const { user }= useUser();
  const { id } = useParams();
  const [announcement, setAnnouncement] = useState(null);
  const [updateModal,setUpdateModal]= useState(false);
  
  useEffect(()=>{
    const detail = async () => {
      try{
        const response = await ApiService.announce.detail(id);
        const data= response.data;

        if(data.result){
          setAnnouncement(data.announcement);
        }else{
          alert(data.message);
        }
      }catch (err) {
        console.error(err);
        alert("공지사항을 불러오는 도중 오류가 발생했습니다.");
        navigate("/announcements");
      }
    }
    detail();
  },[]);

  const handleDelete = async () =>{
    if (!window.confirm("정말로 공지를 삭제하시겠습니까?")) return;
    try{
      const response= await ApiService.announce.delete(id);
      const data= response.data;
      if(data.result){
        alert(data.message);
        navigate("/announcements");
      }else{
        alert(data.message);
      }
    }catch (err){
      console.log(err);
      alert("공지 삭제 도중 오류가 발생하였습니다.");
    }
  }

  if (!announcement) {
    return <p className="text-center py-4">공지사항을 불러오는 중입니다...</p>;
  }

  return (
    <div className="container py-4">
      <div className="card-body">
        <Announcement announcement={announcement}/>
        <button
          type="button"
          className="btn btn-outline-secondary btn-sm me-2"
          onClick={()=>{navigate("/announcements")}}
        >
          돌아가기
        </button>
      </div>

      { user.role === 'MANAGER' && (
      <div className="mt-2">
        <button
          type="button"
          className="btn btn-outline-secondary btn-sm me-2"
          onClick={()=>{setUpdateModal(true)}}
        >
          수정
        </button>
        <button
          type="button"
          className="btn btn-outline-danger btn-sm"
          onClick={()=>{handleDelete()}}
        >
          삭제
        </button>
        {updateModal && (
          <Modal isOpen={updateModal} onClose={()=>setUpdateModal(false)}>
            <AnnouncementUpdateForm announcement={announcement} onCancel={()=>setUpdateModal(false)}/>
          </Modal>
        )}
      </div>    
      )}
    </div>
  );
}

export default AnnouncementDetailPage;