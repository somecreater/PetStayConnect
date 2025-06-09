import React from 'react';

import img from '../../assets/images/img.png';
import img_1 from '../../assets/images/img_1.png';
import img_2 from '../../assets/images/img_2.png';
import { useNavigate } from 'react-router-dom';

//실제 서비스 화면과 연결 시키자(새로운 기능)
//이벤트, 위치, 종 기반 애완동물 호텔 추천, 서비스 공지사항
const services = [
  {
    title: 'Event',
    text: '새로운 이벤트',
    img: img_1,
    url: ''
  },
  {
    title: 'Recommend For You',
    text: '당신에게 필요한 호텔과 서비스를 추천',
    img: img,
    url: '/recommend'
  },
  {
    title: 'Announcement',
    text: '공지 사항',
    img: img_2,
    url: '/announcements'
  },
];

export default function ServicesSection() {

  const navigate = useNavigate();

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
                  <button 
                    className="btn btn-sm btn-warning"
                    onClick={()=>navigate(svc.url)}
                  >
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
