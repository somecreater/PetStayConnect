import React, { useState } from 'react';
import ApiService from '../../common/Api/ApiService';
import { useNavigate } from 'react-router-dom';

function BusinessSearchPage(props){

  const [businesses,setBusinesses] = useState([]);
  const [size] = useState(5);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [search, setSearch]= useState({
    businessName: '',
    sectorCode: '',
    typeCode: '',
    is_around: ''
  });

  const navigate = useNavigate()

  const getBusinessList = async () => {
    const response = await ApiService.business.list(search,page,size);
    const data= response.data;
    if(data.result){
      alert(data.message);
      setBusinesses(data.search);
      setTotalPages()
    }else{
      alert(data.message);
    }
  };
  
  return (
    <>
      <div className='BusinessSearchPage'>
        


      </div>
    </>
  );
}

export default BusinessSearchPage;