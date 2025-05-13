// src/common/Ui/ServicesSection.jsx
import React from 'react';

// public/assets/images 폴더에 강아지 사진을 넣고 경로만 바꿔주세요
const services = [
  {
    title: 'Luxury Suite',
    text: '아늑한 방, 정기 물갈이, 맞춤 급여 제공',
    img: '/assets/images/dog-suite.jpg',
  },
  {
    title: 'Outdoor Walks',
    text: '전문 산책 서비스로 활기찬 하루',
    img: '/assets/images/dog-walk.jpg',
  },
  {
    title: 'Grooming',
    text: '프로 그루머의 목욕·미용 서비스',
    img: '/assets/images/dog-groom.jpg',
  },
];

export default function ServicesSection() {
  return (
    <section className="services-section">
      <div className="container">
        <h2 className="text-center mb-5">Our Services</h2>
        <div className="row gy-4">
          {services.map((svc, i) => (
            <div className="col-12 col-md-6 col-lg-4" key={i}>
              <div className="card service-card h-100">
                <img src={svc.img} className="card-img-top" alt={svc.title} />
                <div className="card-body service-card-body">
                  <h3 className="service-card-title">{svc.title}</h3>
                  <p className="service-card-text">{svc.text}</p>
                  <button className="btn btn-sm btn-success">
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
