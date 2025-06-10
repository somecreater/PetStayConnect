import React, {useEffect, useState} from "react";
import GetLocation from "../Component/GetLocation";
import ApiService from "../../common/Api/ApiService";

// 사용자 위치 기반으로 주변 애완동물 호텔(그외 다양한 사업체)를 추천해주는 페이지
// 사용자 위치 파악 -> 위치 값 기반으로 근처 사업체 추천
function RecommendPage(props){

  //위도:
  const [latitude, setLatitude] = useState(null);
  //경도:
  const [longitude, setLongitude] = useState(null);
  const [errorLoc, setErrorLoc] = useState('');
  const [loadingLoc, setLoadingLoc] = useState(false);

  const [searchType, setSearchType] = useState('HOTEL');
  const [distance, setDistance]= useState(5000)
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);
  const [recommendList, setRecommendList] = useState([]);
  const [totalPage, setTotalPage] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);

  useEffect(() => {
    if (latitude && longitude) {
      handleRecommend();
    }
  }, [page,size]);

  const handleGetLocationAPI = async ()=>{
    setLoadingLoc(true);
    setErrorLoc("");

    try {
      const response= await ApiService.location();
      const data= response.data;

      if(data.result){
        console.log(data);
        const lat = data.location.location.lat;
        const lng = data.location.location.lng;
        setLatitude(lat);
        setLongitude(lng);
        setLoadingLoc(false);
        console.log("latitude: " + lat + " \n"
          + "longitude: " + lng);
      }else{
        alert(data.message);
      }
    } catch (e) {
      setErrorLoc(e.message);
      setLoadingLoc(false);
    }
  };

  const handleRecommend = async () => {
    try{
      console.log("lat: "+latitude
        +"\nlon: "+longitude
        +"\ndis: "+distance
        +"\npag: "+page
        +"\nsiz: "+size
        +"\ntype: "+searchType
      );
      
      const response= await ApiService.recommend(
        latitude, 
        longitude, 
        distance, 
        page, 
        size, 
        searchType);
      const data= response.data;
      if(data.result){
        setRecommendList(data.recommendList.content);
        setTotalPage(data.totalPages);
        setCurrentPage(data.currentPage);
      }else{
        alert(data.message);
      }
    } catch (err) {
      console.error(err);
      alert("추천 불러오기 중 오류가 발생했습니다.");
    }
  };

  const goFirst = () => setPage(0);
  const goPrev = () => setPage(prev => Math.max(0, prev - 1));
  const goNext = () => setPage(prev => Math.min(totalPage - 1, prev + 1));
  const goLast = () => setPage(totalPage - 1);

  return (
    <div className="container py-4">
      <h4 className="mb-3">서비스 추천 설정</h4>
      <div className="card mb-4">
        <div className="card-body">
          <button
            className="btn btn-primary mb-3"
            type="button"
            onClick={handleGetLocationAPI}
          >
            현재 위치 가져오기
          </button>
          {loadingLoc && <p>위치 정보를 가져오는 중...</p>}
          {errorLoc && <p className="text-danger">{errorLoc}</p>}

          {latitude && longitude && (
            <>
              <GetLocation latitude={latitude} longitude={longitude} error={errorLoc} />
              <div className="row g-3 align-items-center mb-3">
                <div className="col-auto">
                  <label className="col-form-label">거리(최대 20km 반경) (m)</label>
                </div>
                <div className="col-auto">
                  <input
                    type="number"
                    className="form-control"
                    value={distance}
                    onChange={(e) =>{
                        if(Number(e.target.value)>20000){
                          setDistance(20000);
                        }  
                        setDistance(Number(e.target.value));
                      }
                    }
                  />
                </div>
                <div className="col-auto">
                  <label className="col-form-label">서비스 종류</label>
                </div>
                <div className="col-auto">
                  <select
                    className="form-select"
                    value={searchType}
                    onChange={(e) => setSearchType(e.target.value)}
                  >
                    <option value="HOTEL">애완동물 호텔</option>
                    <option value="HOSPITAL">애완동물 병원</option>
                    <option value="ETC">기타</option>
                  </select>
                </div>
              </div>
              <button
                className="btn btn-success"
                type="button"
                onClick={handleRecommend}
              >
                추천 서비스 보기
              </button>
            </>
          )}
        </div>
      </div>

      {recommendList.length > 0 && (
        <>
          <div className="row">
            {recommendList.map(item => (
              <div className="col-md-6 mb-3">
                <div className="card h-100">
                  <div className="card-body">
                    <h5 className="card-title">{item.placeName}</h5>
                    <p className="card-text">{item.address}</p>
                    <p className="card-text">{item.phone}</p>
                    <p className="card-text">거리: {Math.round(item.distance)}m</p>
                  </div>
                </div>
              </div>
            ))}
          </div>
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
              <li className={`page-item ${page === totalPage - 1 ? 'disabled' : ''}`}>
                <button className="page-link" onClick={goNext}>다음</button>
              </li>
              <li className={`page-item ${page === totalPage - 1 ? 'disabled' : ''}`}>
                <button className="page-link" onClick={goLast}>마지막</button>
              </li>
            </ul>
          </nav>
        </>
      )}
    </div>
  );
}

export default RecommendPage;
