import React, { useState } from 'react';
import ApiService from '../../common/Api/ApiService';
import { useUser } from '../../common/Context/UserContext';

//여기서는 결제 정보만 전송
function PaymentForm(props){

  const {user} = useUser();
  const {reservation, price, businessName} = props;
  const paymentRequest = {
    pg: 'tosspay',
    pay_method: 'card',
    merchant_uid: `order_${reservation.id}_${reservation.petBusinessId}_${reservation.userId}_${Date.now()}`,
    name: `reservation_${businessName}`,
    amount: price,
    reservation_id: reservation.id,
    buyer_name: user.name,
    buyer_tel: user.phone
  };

  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);

  const onPayment = ()=>{

    const { IMP } = window;
    if (!IMP) {
      setError('결제 스크립트 로드 실패');
      return;
    }
    IMP.init(import.meta.env.VITE_IMP_KEY);

    IMP.request_pay(
      paymentRequest,
      async (response) => {
        if(response.success){

          const payresponse= await ApiService.payments.ready(paymentRequest);
          const data=payresponse.data;

          if(data.result){
            setResult(data.paymentResult);
          }else{
            setError("결제 검증에 실패했습니다.");
          }

        }else{
          setError(response.message);
        }
      }
    );
  };

  const paymentInfo=[
    ['회원님 아이디', user.userLoginId],
    ['서비스 업체 이름', businessName],
    ['서비스 업체 등록번호', reservation.petBusinessRegisterNumber],
    ['체크 인', reservation.checkIn],
    ['체크 아웃', reservation.checkOut],
    ['특별 요구사항', reservation.specialRequests],
    ['사업자 요구정보', reservation.businessRequestInfo],
    ['결제 대행사(PG사)', paymentRequest.pg],
    ['결제 방식', paymentRequest.pay_method],
    ['주문 번호', paymentRequest.merchant_uid],
    ['상품 이름', paymentRequest.name],
    ['결제 가격', paymentRequest.amount]
  ];

  return (
    <div className="container py-4">
      <div className="card mb-3">
        <div className="card-header">
          <strong>결제 정보 확인</strong>
        </div>
        <div style={{ maxHeight: '50vh', overflowY: 'auto' }} className="card-body">
          <ul className="list-group list-group-flush">
            {paymentInfo.map(([label, value]) => (
              <li key={label} className="list-group-item d-flex justify-content-between">
                <span>{label}</span>
                <span>{value}</span>
              </li>
            ))}
          </ul>
          
        </div>
      </div>

      <button onClick={onPayment} className="btn btn-primary w-100">
        결제 요청하기
      </button>

      {result && (
        <div className="alert alert-success mt-3">
          <h5>결제 요청 성공</h5>
          <pre>{JSON.stringify(result, null, 2)}</pre>
        </div>
      )}
      {error && (
        <div className="alert alert-danger mt-3">{error}</div>
      )}
    </div>
  );
}

export default PaymentForm;