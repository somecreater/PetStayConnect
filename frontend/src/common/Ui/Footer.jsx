export default function Footer() {
  return (
    <footer className="footer-custom">
      <div className="container py-4">
  {/* 로고/브랜드명 중앙 정렬 */}
        <div className="footer-logo text-center mb-2">
          PetStory
        </div>
        {/* 회사 및 사업자 정보 */}
        <div className="footer-info text-center small text-secondary" style={{lineHeight: "1.7"}}>
          서울특별시 가상구 상상로 123 미래타워 8층 (주)펫이야기 대표이사 : 홍길동<br />
          사업자등록번호 : 000-00-00000 &nbsp; 개인정보관리책임자 : 김철수 &nbsp; <br />
          통신판매업신고번호 : 제2025-가상구-00001호<br />
          TEL(국번없이) : 1800-0000 &nbsp; FAX : 02-1234-5678 &nbsp; E-MAIL : pethotel@pethotelinfo.com<br />
           (주)펫스토리는 통신판매중개자로서 통신판매의 당사자가 아니며, 상품의 예약, 이용 및 환불 등과 관련한 의무와 책임은 각 판매자에게 있습니다. <br />
             <div className="container d-flex justify-content-between align-items-center py-3">
                            <div>
                              <a href="/about" className="footer-link">About</a>
                              <a href="/contact" className="footer-link">Contact</a>
                            </div>
                          </div>
            ⓒ 2025 PET STORY Corp. All rights reserved.
        </div>

      </div>
    </footer>
  );
  }