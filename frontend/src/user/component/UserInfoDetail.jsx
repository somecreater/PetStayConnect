import React from 'react';
import { useUser } from '../../common/Context/UserContext';
import '../../common/Css/common.css';
import UserProviderInfo from './UserProviderInfo';
import CustomLabel from '../../common/Ui/CustomLabel';
import CusomP from '../../common/Ui/CusomP';

function UserInfoDetail(props){

  const { user }= useUser();

  return (
    <>
    <div className='UserInfoDetail'>

      <CustomLabel classtetxt={'UserInfolabel'} title={'아이디:'} for={'UserInfo'}/> 
      <CusomP classtext={'UserInfo'} title={user.userLoginId}/>

      <CustomLabel classtetxt={'UserInfolabel'} title={'이름:'} for={'UserInfo'}/> 
      <CusomP classtext={'UserInfo'} title={user.name}/>

      <CustomLabel classtetxt={'UserInfolabel'} title={'권한:'} for={'UserInfo'}/> 
      <CusomP classtext={'UserInfo'} title={user.role}/> 

      <CustomLabel classtetxt={'UserInfolabel'} title={'이메일:'} for={'UserInfo'}/> 
      <CusomP classtext={'UserInfo'} title={user.email}/>
      
      <CustomLabel classtetxt={'UserInfolabel'} title={'전화번호:'} for={'UserInfo'}/> 
      <CusomP classtext={'UserInfo'} title={user.phone}/>
      
      <CustomLabel classtetxt={'UserInfolabel'} title={'로그인 타입:'} for={'UserInfo'}/> 
      <CusomP classtext={'UserInfo'} title={user.loginType}/>
      
      <CustomLabel classtetxt={'UserInfolabel'} title={'애완동물 수:'} for={'UserInfo'}/> 
      <CusomP classtext={'UserInfo'} title={user.petNumber}/>
      
      <CustomLabel classtetxt={'UserInfolabel'} title={'QNA점수:'} for={'UserInfo'}/> 
      <CusomP classtext={'UserInfo'} title={user.qnaScore}/>

      <CustomLabel classtetxt={'UserInfolabel'} title={'회원의 포인트:'} for={'UserInfo'}/> 
      <CusomP classtext={'UserInfo'} title={user.point}/>
      
      <CustomLabel classtetxt={'UserInfolabel'} title={'생성일자:'} for={'UserInfo'}/> 
      <CusomP classtext={'UserInfo'} title={user.createAt}/>
      
      <CustomLabel classtetxt={'UserInfolabel'} title={'수정일자:'} for={'UserInfo'}/> 
      <CusomP classtext={'UserInfo'} title={user.updateAt}/>
    </div>
    {
      user.petBusinessDTO!==null&&(
        <UserProviderInfo  petBusinessDTO={user.petBusinessDTO}/>
      )
    }
    </>
  );
  
}

export default UserInfoDetail;