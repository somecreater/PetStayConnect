import React from 'react';

import img from '../../assets/images/img.png';
import img_1 from '../../assets/images/img_1.png';
import img_2 from '../../assets/images/img_2.png';

const services = [
  {
    title: 'Luxury Suite',
    text: '아늑한 방, 정기 물갈이, 맞춤 급여 제공',
    img: img
  },
  {
    title: 'Outdoor Walks',
    text: '전문 애견 미용 서비스',
    img: img_1
  },
  {
    title: 'Grooming',
    text: '프로 그루머의 목욕·미용 서비스',
    img: img_2
  },
];

export default function ServicesSection() {
  return (
    <section className="services-section">
       <div className="services-wrapper my-5 px-5">
        <h2 className="text-center mb-5">Our Services</h2>
        <div className="row gy-4">
          {services.map((svc, i) => (
            <div className="col-12 col-md-6 col-lg-4" key={i}>
              <div className="card service-card h-100">
                <img src={svc.img} className="card-img-top" alt={svc.title} />
                <div className="card-body service-card-body">
                  <h3 className="service-card-title">{svc.title}</h3>
                  <p className="service-card-text">{svc.text}</p>
                  <button className="btn btn-sm btn-warning">
                    자세히 보기
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
