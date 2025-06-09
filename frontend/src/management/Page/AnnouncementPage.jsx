import React, { useState } from "react";
import ApiService from "../../common/Api/ApiService";

function AnnouncementPage(props){
  
  const [announcements,setAnnouncements]= useState([]);
  const [prAnnounce,setPrAnnounce]= useState([]);
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);
  const [totalPages,setTotalPages]= useState(0);

  const getAnnouncement= async () =>{
    try{
      const response= await ApiService.announce.list(page,size);
      const data= response.data;

      if(data.result){
        setAnnouncements(data.announcementDTOS)
        setTotalPages(totalPages)
      }else{
        alert(data.message);
      }

    }catch (err){
      console.log(err);
      alert("공지 가져오는 도중 오류 발생");
    }
  };

  const getPriority= async ()=>{
    try{
      const response= await ApiService.announce.priority();
      const data= response.data;
      if(data.result){
        setPrAnnounce(data.announcementList);
      }else{
        console.log(data.message);
      }
    }catch (err){
      console.log(err);
      alert("특별 공지 가져오는 도중 오류 발생");
    }
  };

  return (
    <>
    </>
  );
}

export default AnnouncementPage;