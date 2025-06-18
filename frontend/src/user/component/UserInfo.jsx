import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import ApiService from '../../common/Api/ApiService';
import '../../common/Css/common.css';
import { useUser } from '../../common/Context/UserContext';
import UserInfoDetail from './UserInfoDetail';
import CustomP from '../../common/Ui/CusomP';
import Button from '../../common/Ui/Button';
import CustomLabel from '../../common/Ui/CustomLabel';
import Modal from '../../common/Ui/Modal';
import Account from './Account';

function UserInfo(props){
  
  const { updateUser,mapUserDto,user }= useUser();
  const [ isDetailRun, setIsDetailRun ]= useState(false);
  const [ isDetail, setIsDetail]=useState(false);
  const navigate=useNavigate();
  const [buttonTitle, setButtonTitle] = useState('자세한 정보 보기');
  const [account,setAccount] = useState(null);
  const [accountModal, setAccountModal]= useState(false);

  const handleInfoClick= async () =>{
    if(!isDetail){
      setIsDetail(true);
      await getuserDetailInfo(user.userLoginId);
      setButtonTitle('간단한 정보 보기');
    }else{
      setIsDetail(false);
      setButtonTitle('자세한 정보 보기');
    }
  }

  const handleAccount = () => {
    getAccount();
    setAccountModal(true);
  }
  const getAccount = async () => {

    const role=user.role;

    const api = (role === 'SERVICE_PROVIDER') ? 
      ApiService.accounts.business : 
      ApiService.accounts.user;

    if(role === 'SERVICE_PROVIDER'){
      const response = await api(user.petBusinessDTO.id);
      const data = response.data;
      
      if(data.result){
        alert(data.message);
        setAccount(data.account);
      }else{
        alert(data.message);
      }
    }else{
      const response = await api();
      const data = response.data;
      
      if(data.result){
        alert(data.message);
        setAccount(data.account);
      }else{
        alert(data.message);
      }
    }

  };

  const getuserDetailInfo = async (UserId) => {
    try{
      const response = await ApiService.userService.detail(UserId);
      if(response.data.auth){
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
  
  const updateUserInfo = async () => {  
    try{
      const response=await ApiService.userService.info();
      if(response.data.auth){
        await getuserDetailInfo(response.data.loginId);
      }
    }catch(err){
      console.error('사용자 정보 가져오기 오류:', err);
    }
  };
  
  useEffect(()=>{
    updateUserInfo();
  },[]);

  return (
    <div className="container py-4">
      <div className="d-flex mb-3">
        <Button
          classtext="me-2"
          type="button"
          title="예약 관리"
          onClick={() => navigate('/user/reservations')}
        />
        <Button
          classtext="me-2"
          type="button"
          title="펫 정보 보기"
          onClick={() => navigate('/user/petmanage')}
        />
        <Button
          classtext="me-2"
          type="button"
          title={buttonTitle}
          onClick={handleInfoClick}
        />
        {user.role === "SERVICE_PROVIDER" &&
        <Button
          classtext="me-2"
          type="button"
          title="사업자 관리"
          onClick={()=>navigate("/business/manage")}        
        />
        }
        <Button
          classtext="me-2"
          type="button"
          title="정산 계좌"
          onClick={handleAccount}
        />

        <Button
          classtext="me-2"
          type="button"
          title="내 리뷰 보기"
          onClick={() => navigate('/reviews/my')}
          />
        {account && accountModal &&
          <Modal isOpen={accountModal} onClose={()=>setAccountModal(false)}>
            <Account account={account}/>
          </Modal>
        }
      </div>

      <div className="card">
        <div className="card-body">
          {isDetail ? (
            <UserInfoDetail />
          ) : (
            <dl className="row mb-0">
              <dt className="col-sm-3">아이디</dt>
              <dd className="col-sm-9">
                <CustomP classtext="mb-0" title={user.userLoginId} />
              </dd>

              <dt className="col-sm-3">이름</dt>
              <dd className="col-sm-9">
                <CustomP classtext="mb-0" title={user.name} />
              </dd>

              <dt className="col-sm-3">권한</dt>
              <dd className="col-sm-9">
                <CustomP classtext="mb-0" title={user.role} />
              </dd>
            </dl>
          )}
        </div>
      </div>
    </div>
  );
}

export default UserInfo;
