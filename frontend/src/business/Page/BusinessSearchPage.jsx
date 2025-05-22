import React, { useState,useEffect, useCallback } from 'react';
import ApiService from '../../common/Api/ApiService';
import { useNavigate } from 'react-router-dom';
import BusinessList from '../Component/BusinessList';
import { useUser } from '../../common/Context/UserContext';
import Button from '../../common/Ui/Button';

function BusinessSearchPage(props){

  const navigate = useNavigate();
  const { user }= useUser();

  const [petList, setPetList] = useState([]);
  const [businesses,setBusinesses] = useState([]);
  const [size, setSize] = useState(5);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  
  const [searchType, setSearchType] = useState('business');
  const [search, setSearch]= useState({
    businessName: '',
    sectorCode: '',
    typeCode: '',
    is_around: false,
    is_external: false,
  });

  const handleChange = e => {
    const { name, type, value, checked } = e.target;
    setSearch(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value,
    }));
  };

  const handleSearchInput = e => {
    const { value } = e.target;
    setSearch(prev => {
      const next = { ...prev };
      if (searchType === 'business') {
        next.businessName = value;
      } else if (searchType === 'type') {
        next.typeCode = value;
      } else{
        next.businessName = value;
        next.typeCode = value;
      }
      return next;
    });
  };

  const getBusinessList = useCallback(async () => {
    try{
      const api = search.is_external
        ? ApiService.business.outerlist
        : ApiService.business.list;
      const resp = await api(search, page, size);
      const data = resp.data;
      if (data.result) {
        const items = Array.isArray(data.search)
        ? data.search
        : data.search.content;
        setBusinesses(items);
        setTotalPages(data.totalPages);
      } else {
        alert(data.message);
      }
    } catch (err) {
      console.error(err);
      alert('목록을 불러오는 중 오류가 발생했습니다.');
    }
  }, [search, page, size]);

  const fetchPets = useCallback(async () => {

    const res = await ApiService.pet.userpet();
    if (res.data.result) {
      setPetList(res.data.pets || []);
    } else {
      console.log('펫 목록을 불러오지 못했습니다: ' + res.data.message);
      setPetList([]);
    }

  }, [user]);

  useEffect(() => {
    fetchPets();
  },[fetchPets]);

  useEffect(() => {
    getBusinessList();
  }, [page, size]);

  const handleSearchClick = () => {
    setBusinesses([]);
    getBusinessList();
  };

  const getRecomendKeyword = () => {
    alert("추천 검색어 목록은 다음과 같습니다 👍 \n"
      + "애완 조류, 관상어, 열대어 소매, 애완동물 소매 \n"
      + "반려동물 사료, 애완동물 사료 소매, 애견용품 소매 \n"
      + "배합사료 제조, 단미사료 제조, 펫푸드 제조 \n"
      + "동물병원, 수의진료, 애완 예방접종, 반려 예방접종, 애완동물 장례식장, 애완동물 화장터 \n"
      + "펫시터, 반려동물 호텔, 애완동물 호텔, 애완동물 미용, 반려동물 미용, 펫 미용, 애완동물 목욕, 반려동물 목욕, 펫 목욕 \n"
      + "유기견 보호센터, 애견훈련소 \n"
      + "반려묘, 고양이 돌봄, 캣 호텔 \n"
      + "반려동물 보험, 펫 보험 \n"
      + "유기묘 보호센터, 반려동물 입양, 동물 구조 \n"
      + "펫 택시, 애완동물 운송, 반려동물 운송 \n"
      + "반려동물 사진관, 애완동물 사진관 \n"
      + "동물 행동교정, 펫 심리상담 \n"
      + "펫 푸드 배달, 반려동물 카페, 애완동물 용품, 반려동물 보행기 \n"
    );
  };

  const goFirst = () => setPage(0);
  const goPrev = () => setPage(prev => Math.max(0, prev - 1));
  const goNext = () => setPage(prev => Math.min(totalPages - 1, prev + 1));
  const goLast = () => setPage(totalPages - 1);


  return (
    <div className="container py-4">
      <form className="row g-3 align-items-end mb-4">
        <div className="col-md-3">
          <label className="form-label">검색 유형</label>
          <select
            className="form-select"
            name="searchType"
            value={searchType}
            onChange={e => setSearchType(e.target.value)}
          >
            {!search.is_external &&
            <option value="business">이름</option>
            }
            <option value="bu_ty">이름+타입</option>
            <option value="type">타입</option>
          </select>
        </div>

        <div className="col-md-2 form-check">
          <input
            className="form-check-input"
            type="checkbox"
            id="isAround"
            name="is_around"
            checked={search.is_around}
            onChange={handleChange}
          />
          <label className="form-check-label" htmlFor="isAround">
            주변만 검색
          </label>
        </div>

        <div className="col-md-2 form-check">
          <input
            className="form-check-input"
            type="checkbox"
            id="isExternal"
            name="is_external"
            checked={search.is_external}
            onChange={handleChange}
          />
          <label className="form-check-label" htmlFor="isExternal">
            외부 데이터
          </label>
        </div>

        <div className="col-md-3">
          <label className="form-label">검색어</label>
          <input
            type="search"
            className="form-control"
            name="businessName"
            value={
              searchType === 'business'
                ? search.businessName
                : searchType === 'type'
                ? search.typeCode
                : search.businessName 
            }
            onChange={handleSearchInput}
            placeholder="검색어를 입력하세요"
          />
        </div>


        <div className="col-md-2 d-grid">
          <Button
            classtext="btn btn-primary"
            type="button"
            title="검색"
            onClick={handleSearchClick}
          />
        </div>

        <div className="col-md-2 d-grid">
          <Button
            classtext="btn btn-primary"
            type="button"
            title="추천 검색어 보기"
            onClick={getRecomendKeyword}
          />
        </div>
      </form>

      {businesses.length > 0 ? (
        <BusinessList List={businesses} petList={petList} isReservation={true} />
      ) : (
        <div className="alert alert-info">검색 결과가 없습니다.</div>
      )}

      <nav className="d-flex justify-content-center mt-4">
        <ul className="pagination">
          <li className={`page-item ${page === 0 ? 'disabled' : ''}`}>
            <button className="page-link" onClick={goFirst}>« First</button>
          </li>
          <li className={`page-item ${page === 0 ? 'disabled' : ''}`}>
            <button className="page-link" onClick={goPrev}>‹ Prev</button>
          </li>

          {Array.from({ length: totalPages }, (_, i) => (
            <li key={i} className={`page-item ${i === page ? 'active' : ''}`}>
              <button className="page-link" onClick={() => setPage(i)}>{i + 1}</button>
            </li>
          ))}

          <li className={`page-item ${page === totalPages - 1 ? 'disabled' : ''}`}>
            <button className="page-link" onClick={goNext}>Next ›</button>
          </li>
          <li className={`page-item ${page === totalPages - 1 ? 'disabled' : ''}`}>
            <button className="page-link" onClick={goLast}>Last »</button>
          </li>
        </ul>
      </nav>
      
    </div>
  );
}

export default BusinessSearchPage;
