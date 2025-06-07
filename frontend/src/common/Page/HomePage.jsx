import React from 'react';
import ServicesSection from '../Ui/ServicesSection.jsx';
import '../../App.css';
import img_3 from '../../assets/images/img_3.png';
import { useNavigate } from 'react-router-dom';

export default function HomePage() {
 const navigate=useNavigate();

 return (
   <>
     <div className="container homepage-container-bg">
       <div className="row d-flex align-items-center gx-3" style={{ minHeight: '400px' }}>
          <div className="col-12 col-md-6 py-5 pe-md-1 text-center">
              <h1 className="display-4">Pet Service Booking System</h1>
              <p className="lead mb-4">Easily book appointment online for pet care services</p>
              <button 
                className="btn btn-warning btn-lg"
                onClick={()=>navigate('/business/list')}
              >
                Book Now
              </button>
          </div>
          <div className="col-12 col-md-6 py-5 ps-md-1 text-center">
                 <img
                   src={img_3}
                   alt="Pet Illustration"
                   className="img-fluid"
                   style={{ maxWidth: '300px', margin: '0 auto', display: 'block' }}
                 />
          </div>
       </div>
     </div>

      {/* Services 카드 섹션 */}
      <ServicesSection />
    </>
  );
}
