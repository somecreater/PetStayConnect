// src/common/Page/HomePage.jsx
import React from 'react';
import ServicesSection from '../Ui/ServicesSection.jsx';
import '../../App.css';

export default function HomePage() {
 return (
   <>
     <div className="container">
       <div className="row d-flex align-items-center" style={{ minHeight: '400px' }}>
         <div className="col py-5">
          <div className="col-md-6 text-center text-md-start mb-4 mb-md-0">
            <h1 className="display-4">Pet Service Booking System</h1>
            <p className="lead mb-4">Easily book appointment online for pet care services</p>
            <button className="btn btn-warning btn-lg">Book Now</button>
          </div>
         </div>
         <div className="col py-5">
           2 of 2
         </div>
       </div>
     </div>

      {/* Services 카드 섹션 */}
      <ServicesSection />
    </>
  );
}
