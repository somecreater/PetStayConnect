import React from 'react';
import { useNavigate } from 'react-router-dom';
import DeleteForm from '../Form/DeleteForm';
import '../../common/Css/common.css';

function DeletePage(props){

  const navigate = useNavigate();

  return (
    <>
      <div className="UserDeletePage">

        <DeleteForm/>

      </div>
    </>
  );
}

export default DeletePage;