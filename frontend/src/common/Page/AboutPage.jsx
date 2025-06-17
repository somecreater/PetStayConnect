import React from 'react';
export default function AboutPage() {
  
  return (
    <div className="container py-5">
      <div className="text-center mb-5">
        <h1 className="fw-bold mb-3">🐾 PetStory 서비스 소개</h1>
        <p className="lead text-secondary">
          반려동물과 보호자가 모두 만족할 수 있는 <strong className="text-orange">스마트한 펫 서비스 플랫폼</strong>
        </p>
      </div>

      <div className="row g-4">
        {sections.map((section, index) => (
          <div key={index} className="col-12">
            <div className="p-4 rounded-4 shadow-sm bg-white border border-light">
              <h4 className="mb-3">{section.icon} {section.title}</h4>
              <ul className="list-unstyled mb-0">
                {section.items.map((item, idx) => (
                  <li key={idx} className="mb-1">{item}</li>
                ))}
              </ul>
            </div>
          </div>
        ))}
      </div>

      <div className="text-center mt-5">
        <hr />
        <p className="text-muted small">
          PetStory는 여러분과 반려동물이 더 행복한 삶을 살아갈 수 있도록 언제나 노력하겠습니다. 🐶🐱
        </p>
      </div>
    </div>
  );
}
const sections = [
  {
    icon: '🔐',
    title: '회원가입 및 로그인',
    items: [
      '사용자와 사업자 두 가지 회원 유형 지원',
      'Google 계정으로 간편 가입 가능',
      '사업자는 사업자 등록번호 인증을 통해 소비자 신뢰 확보'
    ]
  },
  {
    icon: '🐶',
    title: '반려동물 관리',
    items: [
      '여러 마리의 반려동물 등록 가능',
      '건강 정보와 특이사항 기록',
      '견종/묘종 목록 참고 가능'
    ]
  },
  {
    icon: '📌',
    title: '관심 목록',
    items: [
      '호텔 및 QnA 게시글 즐겨찾기 가능'
    ]
  },
  {
    icon: '🏨',
    title: '호텔 검색 및 예약',
    items: [
      '지역, 사업체명, 태그 등으로 검색 가능',
      '체크인/아웃, 반려동물 선택 등 예약 절차 제공',
      '이미 결제가 완료되었고 기한이 지난 예약에 대해 리뷰 작성 가능'
    ]
  },
  {
    icon: '💳',
    title: '결제 및 포인트',
    items: [
      '결제 후 수수료 제외한 금액 사업자 정산',
      '포인트 적립 및 30% 할인 결제 가능'
    ]
  },
  {
    icon: '📅',
    title: '예약 관리 및 정산',
    items: [
      '사업자용 대시보드 제공',
      '모든 결제 내역 및 잔액 확인 가능'
    ]
  },
  {
    icon: '❓',
    title: 'QnA 및 사용자 소통',
    items: [
      '반려동물 관련 자유 질문 및 답변 기능',
      '답변 채택 시 사업자 점수 부여 및 상단 노출'
    ]
  },
  {
    icon: '📍',
    title: '위치 기반 추천',
    items: [
      '20km 반경 내 호텔/병원 정보 제공',
      '거리순 정렬로 간편 확인'
    ]
  }
];