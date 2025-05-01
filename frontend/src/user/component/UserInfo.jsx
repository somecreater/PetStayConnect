import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ApiService from '../../common/Api/ApiService';
import '../../common/Css/common.css';
import { useUser } from '../../common/Context/UserContext';
import UserInfoDetail from './UserInfoDetail';
import CustomP from '../../common/Ui/CusomP';
import Button from '../../common/Ui/Button';
import CustomLabel from '../../common/Ui/CustomLabel';

function UserInfo(props){
  
  const { updateUser,mapUserDto,user }= useUser();
  const [ isDetailRun, setIsDetailRun ]= useState(false);
  const [ isDetail, setIsDetail]=useState(false);
  const navigate=useNavigate();
  const [buttonTitle, setButtonTitle] = useState('자세한 정보 보기');

  useEffect(() => {
    if (!user || user.userLoginId === '') {
      getuserInfo();
    }
  }, []);
  const handleInfoClick=() =>{
    if(isDetail === false && isDetailRun === false){
      setIsDetail(true);
      setIsDetailRun(true);
      getuserDetailInfo(user.userLoginId);
      setButtonTitle('간단한 정보 보기');
    }else{
      setIsDetail(false);
      setButtonTitle('자세한 정보 보기');
    }
  }

  const getuserDetailInfo = async (UserId) => {
    try{

      const response = await ApiService.userService.detail(UserId);
      if(response.data.auth){
        console.log('get detail user');
        const mapped=response.data.userDetail;
        updateUser(mapUserDto(mapped));
      }else{
        console.log('자세한 사용자 정보 없음');
      }

    }catch(err){
      console.error('자세한 사용자 정보 가져오기 오류:', err);
    }
  };

  const getuserInfo = async () => {
    try{
      const response=await ApiService.userService.info();

      if(response.data.auth){
        updateUser({
          name: response.data.UserName,
          email: null,
          userLoginId: response.data.loginId,
          role: response.data.Role, 
        });
      }else{
        alert(response.data.message);
        navigate('/user/login');
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
      <div>
        <Button 
          classtext="petButton" 
          type="button" 
          title={'펫 정보 보기기'}
          onClick={()=>navigate('/user/petmanage')}
        />
        <Button 
          classtext="detailButton" 
          type="button" 
          title={buttonTitle}
          onClick={handleInfoClick}
        />
        { isDetail === true ?(
            <UserInfoDetail/>
        ):(
          <div className='UserInfo'>

            <CustomLabel classtetxt={'UserInfolabel'} title={'아이디:'} for={'UserLoginId'}/> 
            <CustomP classtext={'UserLoginId'} title={user.userLoginId}/>

            <CustomLabel classtetxt={'UserInfolabel'} title={'이름:'} for={'UserName'}/> 
            <CustomP classtext={'UserName'} title={user.name}/>

            <CustomLabel classtetxt={'UserInfolabel'} title={'권한:'} for={'UserRole'}/> 
            <CustomP classtext={'UserRole'} title={user.role}/> 

          </div>
        )}
      </div>
    </>
  );
}

export default UserInfo;
