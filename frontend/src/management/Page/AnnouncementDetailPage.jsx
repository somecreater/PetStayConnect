import React, { useState } from "react";
import { useUser } from "../../common/Context/UserContext";
import Announcement from "../component/Announcement";
import Modal from "../../common/Ui/Modal";
import AnnouncementUpdateForm from "../Form/AnnouncementUpdateForm";
import ApiService from "../../common/Api/ApiService";

function AnnouncementDetailPage(props){
  
  const [user]= useUser();
  const {announcement} = props;
  const [updateModal,setUpdateModal]= useState(false);
  
  const handleDelete = async () =>{
    try{
      const response= await ApiService.announce.delete(announcement.id);
      const data= response.data;
      if(data.result){
        alert(data.message);
      }else{
        alert(data.message);
      }
    }catch (err){
      console.log(err);
      alert("공지 삭제 도중 오류가 발생하였습니다.");
    }
  }

  return (
    <div className="container py-4">
      <div className="card-body">
        <Announcement announcement={announcement}/>
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
            <AnnouncementUpdateForm />
          </Modal>
        )}
      </div>    
      )}
    </div>
  );
}

export default AnnouncementDetailPage;