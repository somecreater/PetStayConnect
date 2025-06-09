import React, { useState, useEffect } from "react";
import ApiService from "../../common/Api/ApiService";
import AnnouncementList from "../component/AnnouncementList";
import { useNavigate } from "react-router-dom";
import { useUser } from "../../common/Context/UserContext";

function AnnouncementPage(props){
  
  const navigate= useNavigate();
  const {user} = useUser();
  const [announcements,setAnnouncements]= useState([]);
  const [priorityList, setPriorityList] = useState([]);
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);
  const [totalPages,setTotalPages]= useState(0);

  //일반 공지 가져오기
  const fetchAnnouncements= async () =>{
    try{
      const response= await ApiService.announce.list(page,size);
      const data= response.data;

      if(data.result){
        setAnnouncements(data.announcementList)
        setTotalPages(data.totalPages)
      }else{
        alert(data.message);
      }

    }catch (err){
      console.log(err);
      alert("공지 가져오는 도중 오류 발생");
    }
  };

  //특별 공지 가져오기(공지 목록에서 상단에 위치, 없다면 그냥 일반공지만 보여준다)
  const fetchPriority= async ()=>{
    try{
      const response= await ApiService.announce.priority();
      const data= response.data;
      if(data.result){
        setPriorityList(data.announcementList);
      }else{
        console.log(data.message);
      }
    }catch (err){
      console.log(err);
      alert("특별 공지 가져오는 도중 오류 발생");
    }
  };

  useEffect(() => {
    fetchPriority();
  }, []);

  useEffect(() => {
    fetchAnnouncements();
  }, [page]);

  const goFirst = () => setPage(0);
  const goPrev = () => setPage(prev => Math.max(0, prev - 1));
  const goNext = () => setPage(prev => Math.min(totalPages - 1, prev + 1));
  const goLast = () => setPage(totalPages - 1);

  return (
    <div className="container py-4">
      {priorityList.length > 0 && (
        <>
          <h2 className="mb-3">중요 공지</h2>
          <AnnouncementList list={priorityList} />
        </>
      )}

      <h2 className="mb-3">공지사항</h2>
      {announcements.length > 0 ? (
        <AnnouncementList list={announcements} />
      ) : (
        <p className="text-center">등록된 공지가 없습니다.</p>
      )}

      {totalPages > 1 &&
        <nav aria-label="Page navigation">
          <ul className="pagination justify-content-center">
            <li className={`page-item ${page === 0 ? 'disabled' : ''}`}>
              <button className="page-link" onClick={goFirst}>처음</button>
            </li>
            <li className={`page-item ${page === 0 ? 'disabled' : ''}`}>
              <button className="page-link" onClick={goPrev}>이전</button>
            </li>
            {Array.from({ length: totalPage }).map((_, idx) => (
              <li key={idx} className={`page-item ${page === idx ? 'active' : ''}`}>
                <button className="page-link" onClick={() => setPage(idx)}>{idx + 1}</button>
              </li>
            ))}
            <li className={`page-item ${page === totalPages - 1 ? 'disabled' : ''}`}>
              <button className="page-link" onClick={goNext}>다음</button>
            </li>
            <li className={`page-item ${page === totalPages - 1 ? 'disabled' : ''}`}>
              <button className="page-link" onClick={goLast}>마지막</button>
            </li>
          </ul>
        </nav>
      }
      
        <div className="d-flex justify-content-end mt-3">
          {user.role === 'MANAGER' &&
            <button className="btn btn-dark" onClick={() => navigate('/announcements/register')}>
              공지사항 쓰기
            </button>
          }
        </div>
    </div>
  );
}

export default AnnouncementPage;