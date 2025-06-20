import React, { useState } from 'react';
import UserInfo from '../component/UserInfo';
import LogoutButton from '../component/LogoutButton';
import UpdateButton from '../component/UpdateButton';
import DeleteButton from '../component/DeleteButton';
import BookmarkList from '../../bookmark/BookmarkList';
import '../../common/Css/common.css';
import Modal from '../../common/Ui/Modal';
import ApiService from '../../common/Api/ApiService';

function UserInfoPage(props){
  
  const [passwordModal,setPasswordModal]= useState(false);
  const [userLoginId,setUserLoginId] = useState('');
  const [oldPassword,setOldPassword] = useState('');
  const [newPassword,setNewPassword] = useState('');

  const psModal = () => {
    setPasswordModal(prev => !prev);
    setUserLoginId('');
    setOldPassword('');
    setNewPassword('');
  }

  const resetPassword = async () => {
    try{
      const request= {
        userLoginId: userLoginId,
        oldPassword: oldPassword,
        newPassword: newPassword
      }

      const response= await ApiService.userService.updatePassword(request);
      const data = response.data;
      if(data.auth){
        alert(data.message);
        psModal();
      }else{
        alert(data.message);
      }
    }catch(err){
      console.log(err);
    }
  }

  return (
    <div className="container py-4">
      <div className="row mb-4">
        <div className="col">
          <UserInfo />
        </div>
      </div>
      <div className="d-flex gap-2 mb-4">
        <LogoutButton />
        <UpdateButton />
        <button
          className="btn btn-outline-primary"
          type="button"
          onClick={()=>psModal()}
        >
          비밀번호 수정
        </button>
        <Modal isOpen={passwordModal} onClose={()=>psModal()}>
          
          <div className="mb-3">
            <input
              type='text'
              className='form-control'
              value={userLoginId}
              placeholder='아이디'
              onChange={e => setUserLoginId(e.target.value)}
            />
          </div>
          <div className="mb-3">
            <input
              type='password'
              className='form-control'
              value={oldPassword}
              placeholder='비밀번호'
              onChange={e => setOldPassword(e.target.value)}
            />
          </div>
          <div className="mb-3">
            <input
              type='password'
              className='form-control'
              value={newPassword}
              placeholder='새 비밀번호'
              onChange={e => setNewPassword(e.target.value)}
            />
          </div>

          <button
            className='btn btn-success w-100'
            type='button'
            onClick={()=>resetPassword()}
          >
            비밀전호 수정
          </button>
        </Modal>
        <DeleteButton />
      </div>
 {/* 북마크 리스트 추가 */}
      <div className="row">
        <div className="col">
          <div className="card p-3 shadow-sm" style={{ background: '#fffbe7', borderRadius: '1rem' }}>
            <BookmarkList />
          </div>
        </div>
      </div>
    </div>
  );
}
export default UserInfoPage;
