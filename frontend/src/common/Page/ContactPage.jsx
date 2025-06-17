import React from 'react';
export default function ContactPage() {
  return (
    <div className="container py-5">
      <div className="text-center mb-5">
        <h1 className="fw-bold text-orange">📞 고객센터 안내</h1>
        <p className="lead text-muted">
          PetStory에 궁금한 점이 있다면 언제든지 연락 주세요!
        </p>
      </div>

      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card shadow-sm border-0 rounded-4 p-4 bg-white text-center">
            <h4 className="mb-3">대표 번호</h4>
            <p className="fs-4 fw-semibold text-dark">📱 1800-0000</p>

            <hr />

            <h5 className="mt-4">운영 시간</h5>
            <p className="mb-1">월–금: 09:00 ~ 18:00</p>
            <p className="text-muted small">* 주말 및 공휴일은 휴무입니다.</p>

            <div className="mt-4">
              <p className="mb-1">✉️ 이메일 문의</p>
              <a href="mailto:support@petstory.com" className="text-decoration-none text-orange fw-medium">
                support@petstory.com
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
