import React from 'react';
import UserInfo from '../component/UserInfo';
import LogoutButton from '../component/LogoutButton';
import UpdateButton from '../component/UpdateButton';
import DeleteButton from '../component/DeleteButton';
import '../../common/Css/common.css';

function UserInfoPage(props){
  
  return (
    <div className="container py-4">
      <div className="row mb-4">
        <div className="col">
          <UserInfo />
        </div>
      </div>
      <div className="d-flex gap-2">
        <LogoutButton />
        <UpdateButton />
        <DeleteButton />
      </div>
    </div>
  );
}

export default UserInfoPage;
