import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../../common/Css/common.css';
import { useUser } from '../../common/Context/UserContext';
import CustomP from '../../common/Ui/CusomP';
function UserInfo(props){
  
  const { user }= useUser();

  return (
    <>
      <div className='UserInfo'>
        <label>아이디: </label> <CustomP classtext={'UserLoginId'} title={user.userLoginId}/>
        <label>이름: </label><CustomP classtext={'UserName'} title={user.name}/>
        <label>권한: </label><CustomP classtext={'UserRole'} title={user.role}/>
        <label>이메일: </label><CustomP classtext={'UserEmail'} title={user.email}/>
      </div>
    </>
  );
}

export default UserInfo;