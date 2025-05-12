import React, {useState} from 'react';
import { useUser } from '../../common/Context/UserContext';
import CustomLabel from '../../common/Ui/CustomLabel';
import CusomP from '../../common/Ui/CusomP';
import '../../common/Css/common.css';
import Button from '../../common/Ui/Button';
import Modal from '../../common/Ui/Modal';
import BusinessValidationForm from '../Form/BusinessValidationForm';

function UserProviderInfo({petBusinessDTO}){
  const {
    businessName,
    status,
    minPrice,
    maxPrice,
    facilities,
    description,
    avgRate,
    registrationNumber,
    bankAccount,
    varification,
    province,
    city,
    town
  } = petBusinessDTO;

  const [isModalOpen,setIsModalOpen]=useState(false);

  const openModal = () => {
    setIsModalOpen(true);
  };
  const closeModal = () => {
    setIsModalOpen(false);
  };

  return(
    <div className="card mb-4">
      <div className="card-header">제공 사업체 정보</div>
      <div className="card-body">
        <dl className="row g-2 mb-4">
          <dt className="col-sm-3">사업체 이름</dt>
          <dd className="col-sm-9">
            <CusomP classtext="ProviderInfo" title={businessName} />
          </dd>

          <dt className="col-sm-3">현재 상태</dt>
          <dd className="col-sm-9">
            <CusomP classtext="ProviderInfo" title={status} />
          </dd>

          <dt className="col-sm-3">최소 가격</dt>
          <dd className="col-sm-9">
            <CusomP classtext="ProviderInfo" title={minPrice} />
          </dd>

          <dt className="col-sm-3">최대 가격</dt>
          <dd className="col-sm-9">
            <CusomP classtext="ProviderInfo" title={maxPrice} />
          </dd>
        </dl>

        <div className="mb-4">
          <label className="form-label">서비스 요약</label>
          <textarea
            className="form-control"
            rows={3}
            readOnly
            value={facilities || ''}
          />
        </div>

        <div className="mb-4">
          <label className="form-label">서비스 설명</label>
          <textarea
            className="form-control"
            rows={3}
            readOnly
            value={description || ''}
          />
        </div>

        <dl className="row g-2 mb-4">
          <dt className="col-sm-3">평점</dt>
          <dd className="col-sm-9">
            <CusomP classtext="ProviderInfo" title={avgRate} />
          </dd>

          <dt className="col-sm-3">사업자 번호</dt>
          <dd className="col-sm-9">
            <CusomP classtext="ProviderInfo" title={registrationNumber} />
          </dd>

          <dt className="col-sm-3">계좌번호</dt>
          <dd className="col-sm-9">
            <CusomP classtext="ProviderInfo" title={bankAccount || '미등록'} />
          </dd>

          <dt className="col-sm-3">인증 현황</dt>
          <dd className="col-sm-9">
            <CusomP classtext="ProviderInfo" title={varification} />
          </dd>

          <dt className="col-sm-3">주소</dt>
          <dd className="col-sm-9">
            <CusomP
              classtext="ProviderInfo"
              title={`${province || ''} ${city || ''} ${town || ''}`.trim()}
            />
          </dd>
        </dl>

        {varification === 'NONE' ? (
          <div className="mt-3">
            <CusomP classtext="ProviderInfo" title="인증을 진행해주세요!" />
            <Button
              classtext="btn btn-primary mt-2"
              type="button"
              title="사업자 인증"
              onClick={openModal}
            />
            <Modal isOpen={isModalOpen} onClose={closeModal}>
              <BusinessValidationForm />
            </Modal>
          </div>
        ) : (
          <div className="mt-3">
            <CusomP
              classtext="ProviderInfo"
              title="이미 인증이 완료된 사업자입니다."
            />
          </div>
        )}
      </div>
    </div>
  );
}

export default UserProviderInfo;
