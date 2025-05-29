import React, { useEffect, useState } from 'react';
import Business from './Business';
import Modal from '../../common/Ui/Modal';
import ReservationForm from '../Form/ReservationForm';
import { useUser } from '../../common/Context/UserContext';

function BusinessList({List, isReservation, petList }){
  
  //추후 예약 버튼 추가, 페이징 처리는 Page 컴포넌트에 추가 
  
  const [isModalOpen,setIsModalOpen]=useState(false);
  const [selectedBusiness, setSelectedBusiness] = useState(null);
  const { user } =useUser();
  
  const openModal = (business) => {
    setSelectedBusiness(business);
    setIsModalOpen(true);
  };
  const closeModal = () => {
    setSelectedBusiness(null);
    setIsModalOpen(false);
  };

  return (
    <div className="container-fluid py-4">
      <div className="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4 overflow-auto align-items-start"
        style={{ maxHeight: '70vh' }}
      >
        {List.map(business => (
          <div key={business.id} className="col">
            <div className="card">
              <div className="card-body d-flex flex-column">
                <Business businesssDTO={business} />
                {(isReservation && business.id) ? (
                  <button
                    className="btn btn-primary mt-auto"
                    onClick={() => openModal(business)}
                  >
                    예약하기
                  </button>
                ):
                <div className="alert alert-info mt-auto mb-0 text-center">
                  외부 데이터여서 예약이 불가능합니다.
                </div>}
              </div>
            </div>
          </div>
        ))}
      </div>

      {selectedBusiness && (
        <Modal isOpen={true} onClose={closeModal}>
          <ReservationForm
            user_login_id={user.userLoginId}
            business_register_number={selectedBusiness.registrationNumber}
            petList={petList}
            business={selectedBusiness}
          />
        </Modal>
      )}
    </div>
  );
}

export default BusinessList;
