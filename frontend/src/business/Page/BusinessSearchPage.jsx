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
        setBusinesses(data.search.content);
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
      alert('펫 목록을 불러오지 못했습니다: ' + res.data.message);
      setPetList([]);
    }

  }, [user]);

  useEffect(() => {
    fetchPets();
  },[fetchPets]);

  useEffect(() => {
    getBusinessList();
  }, [page]);

  const handleSearchClick = () => {
    setPage(0);
    setBusinesses([]);
    getBusinessList();
  };

  const goPrevious = () => {
    setPage(prev => Math.max(0, prev - 1));
  };
  const goNext = () => {
    setPage(prev => Math.min(totalPages - 1, prev + 1));
  };

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
      </form>

      {businesses.length > 0 ? (
        <BusinessList List={businesses} petList={petList} isReservation />
      ) : (
        <div className="alert alert-info">검색 결과가 없습니다.</div>
      )}

        <nav className="d-flex justify-content-center mt-4">
          <ul className="pagination">
            <li className="page-item">
              <button
                className="page-link"
                onClick={goPrevious}
              >
                Previous
              </button>
            </li>
            <li className="page-item disabled">
              <span className="page-link">
                {page + 1} / {totalPages}
              </span>
            </li>
            <li className="page-item">
              <button
                className="page-link"
                onClick={goNext}
              >
                Next
              </button>
            </li>
          </ul>
        </nav>
      
    </div>
  );
}

export default BusinessSearchPage;
