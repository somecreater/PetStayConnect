import React from 'react';
import UserInfo from '../component/UserInfo';
import '../../common/Css/common.css';
import LogoutButton from '../component/LogoutButton';

function UserInfoPage(props){
  
  return (
    <>
      <div className='UserInfoPage'>

        <UserInfo/>
        <LogoutButton/>
        
      </div>
    </>
  );
}

export default UserInfoPage;