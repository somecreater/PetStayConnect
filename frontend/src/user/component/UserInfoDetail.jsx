import React from 'react';
import { useUser } from '../../common/Context/UserContext';
import UserProviderInfo from './UserProviderInfo';
import CustomLabel from '../../common/Ui/CustomLabel';
import CusomP from '../../common/Ui/CusomP';

function UserInfoDetail(props) {
  const { user } = useUser();

  return (
    <>
      <div className="card mb-4">
        <div className="card-header">상세 사용자 정보</div>
        <div className="card-body">
          <dl className="row mb-0">
            <dt className="col-sm-3">아이디</dt>
            <dd className="col-sm-9">
              <CusomP classtext="UserInfo" title={user.userLoginId} />
            </dd>

            <dt className="col-sm-3">이름</dt>
            <dd className="col-sm-9">
              <CusomP classtext="UserInfo" title={user.name} />
            </dd>

            <dt className="col-sm-3">권한</dt>
            <dd className="col-sm-9">
              <CusomP classtext="UserInfo" title={user.role} />
            </dd>

            <dt className="col-sm-3">이메일</dt>
            <dd className="col-sm-9">
              <CusomP classtext="UserInfo" title={user.email || '미등록'} />
            </dd>

            <dt className="col-sm-3">전화번호</dt>
            <dd className="col-sm-9">
              <CusomP classtext="UserInfo" title={user.phone || '미등록'} />
            </dd>

            <dt className="col-sm-3">로그인 타입</dt>
            <dd className="col-sm-9">
              <CusomP classtext="UserInfo" title={user.loginType} />
            </dd>

            <dt className="col-sm-3">애완동물 수</dt>
            <dd className="col-sm-9">
              <CusomP classtext="UserInfo" title={user.petNumber} />
            </dd>

            <dt className="col-sm-3">QNA 점수</dt>
            <dd className="col-sm-9">
              <CusomP classtext="UserInfo" title={user.qnaScore} />
            </dd>

            <dt className="col-sm-3">포인트</dt>
            <dd className="col-sm-9">
              <CusomP classtext="UserInfo" title={user.point} />
            </dd>

            <dt className="col-sm-3">생성일자</dt>
            <dd className="col-sm-9">
              <CusomP classtext="UserInfo" title={user.createAt} />
            </dd>

            <dt className="col-sm-3">수정일자</dt>
            <dd className="col-sm-9">
              <CusomP classtext="UserInfo" title={user.updateAt} />
            </dd>
          </dl>
        </div>
      </div>

      {user.petBusinessDTO && (
        <div className="container">
          <h6 className="mb-3">제공 사업체 정보</h6>
          <UserProviderInfo petBusinessDTO={user.petBusinessDTO} />
        </div>
      )}
    </>
  );
}

export default UserInfoDetail;
