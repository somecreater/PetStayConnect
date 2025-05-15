import React from 'react';
import UserInfo from '../component/UserInfo';
import LogoutButton from '../component/LogoutButton';
import UpdateButton from '../component/UpdateButton';
import DeleteButton from '../component/DeleteButton';
import BookmarkList from '../../bookmark/BookmarkList';
import '../../common/Css/common.css';

function UserInfoPage(props){
  
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
