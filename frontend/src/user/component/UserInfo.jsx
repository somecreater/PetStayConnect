import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { API_ENDPOINTS, createHeaders   } from '../../common/Api/Api';
import RefreshApi from '../../common/Api/RefreshApi';
import '../../common/Css/common.css';
import { useUser } from '../../common/Context/UserContext';
import CustomP from '../../common/Ui/CusomP';
function UserInfo(props){
  
  const { updateUser,user }= useUser();
  const navigate=useNavigate();

  useEffect(() => {
    if (!user || user.userLoginId === '') {
      getuserInfo();
    }
  }, []);

  const getuserDetailInfo = async (UserId) => {
    try{
      const response = await RefreshApi.get(API_ENDPOINTS.auth.detail, {
        params: { userLoginId: UserId },
        headers: createHeaders(),
      });
      
      if(response.data.auth){
        console.log('get detail user');
        updateUser({
          name: response.data.userDetail.name,
          email: response.data.userDetail.email,
          userLoginId: response.data.userDetail.userLoginId,
          role: response.data.userDetail.role, 
        });
      }else{
        console.log('자세한 사용자 정보 없음');
      }
    }catch(err){
      console.error('자세한 사용자 정보 가져오기 오류:', err);
    }
  };

  const getuserInfo = async () => {
    try{
      const response=await RefreshApi.get(API_ENDPOINTS.auth.info,{
        headers: createHeaders(),
      });

      if(response.data.auth){
        updateUser({
          name: response.data.UserName,
          email: null,
          userLoginId: response.data.loginId,
          role: response.data.Role, 
        });
      }else{
        alert(response.data.message);
        navigate('/login');
      }
    }catch(err){
      console.error('사용자 정보 가져오기 오류:', err);
    }
  };
  

  if (!user) {
    return <h2>Not User Information</h2>;
  }

  return (
    <>
      <div className='UserInfo'>
        <label>아이디: </label> <CustomP classtext={'UserLoginId'} title={user.userLoginId}/>
        <label>이름: </label><CustomP classtext={'UserName'} title={user.name}/>
        <label>권한: </label><CustomP classtext={'UserRole'} title={user.role}/>
        { user.email !== null 
        ? (<>
          <label>이메일: </label>
          <CustomP classtext={'UserEmail'} title={user.email} />
          </>)
        : null
        }
      </div>
    </>
  );
}

export default UserInfo;