import React from 'react';
import { useNavigate } from 'react-router-dom';
import UpdateForm from '../Form/UpdateForm';
import '../../common/Css/common.css';

function UpdatePage(props){

  const navigate = useNavigate();

  return (
    <>
    
      <UpdateForm/>

    </>
  );
}

export default UpdatePage;
