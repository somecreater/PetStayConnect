import React from 'react';
import Business from './Business';
import Modal from '../../common/Ui/Modal';
import ReservationForm from '../Form/ReservationForm';
import { useUser } from '../../common/Context/UserContext';

function BusinessList({List, isReservation, roomList, petList }){
  
  //추후 예약 버튼 추가, 페이징 처리는 Page 컴포넌트에 추가 

  const [isModalOpen,setIsModalOpen]=useState(false);
  const { user } =useUser();

  const openModal = () => {
    setIsModalOpen(true);
  };
  const closeModal = () => {
    setIsModalOpen(false);
  };

  return (
    <>
      <div className='BusinessList'>
          {List.map(business=>{
            <div key={business.id}>
              <Business businesssDTO={business}/>
              {
                isReservation && 
                <div className='reservation'>
                  <button className='reservation_button' type='button' onClick={openModal}>예약하기 </button>
                  <Modal isOpen={isModalOpen} onClose={closeModal}>
                    <ReservationForm  
                      user_login_id={user.userLoginId} 
                      business_register_number={business.registrationNumber} 
                      roomList={roomList} 
                      petList={petList} 
                      business={business}
                    />
                  </Modal>
                </div>
              }

            </div>
          })}
      </div>
    </>
  );
}

export default BusinessList;