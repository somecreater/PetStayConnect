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
    <div className="container py-4">
      <div className="row g-4 overflow-auto"
        style={{ maxHeight: '70vh' }}
      >
        {List.map(business => (
          <div key={business.id} className="col-md-6 col-lg-4">
            <div className="card h-100">
              <div className="card-body d-flex flex-column">
                <Business businesssDTO={business} />
                {isReservation && (
                  <button
                    className="btn btn-primary mt-auto"
                    onClick={() => openModal(business)}
                  >
                    예약하기
                  </button>
                )}
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
