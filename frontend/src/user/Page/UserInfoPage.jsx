import React from 'react';
import UserInfo from '../component/UserInfo';
import LogoutButton from '../component/LogoutButton';
import UpdateButton from '../component/UpdateButton';
import DeleteButton from '../component/DeleteButton';
import '../../common/Css/common.css';

function UserInfoPage(props){
  
  return (
    <>
      <div className='UserInfoPage'>

        <UserInfo/>
        <LogoutButton/>
        <UpdateButton/>
        <DeleteButton/>
        
      </div>
    </>
  );
}

export default UserInfoPage;