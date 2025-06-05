import React, { useEffect, useState } from 'react';
import ApiService from '../../common/Api/ApiService';
import { useUser } from '../../common/Context/UserContext';

//여기서는 결제 정보만 전송
function PaymentForm({ reservation, price = 0, businessName }){

  const {user} = useUser();
  const [petList,setPetList]= useState([]);
  const [computedPrice, setComputedPrice] = useState(price);
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);
  const [initialLoading, setInitialLoading] = useState(false);

  //가격의 기본값은 애완동물 사업자에서 설정된 최대 가격 * 예약된 애완동물 수
  useEffect(()=>{
    if(price === 0){
      const fetchBusinessDetail = async () => {
      try {
        const response= await ApiService.business.detail(reservation.petBusinessId);
        const data=response.data;

        const response2= await ApiService.pet.reservation(reservation.id);
        const data2= response2.data;

        if(data.result && data2.result){
          const servicePrice=data.business.maxPrice;
          setPetList(data2.pets);
          
          const total = servicePrice * data2.pets.length;
          setComputedPrice(total);
        }else{
          setError('서비스 가격 정보를 불러오지 못했습니다.');
        }
        
      } catch (e) {
        console.error(e);
        setError('서버 통신 중 오류가 발생했습니다.');
      } finally {
        setInitialLoading(false);
      }
    }
    fetchBusinessDetail();
    }
  },[]);

  /*
  포인트를 이용한 30% 할인 결제 함수로 기존의 결제창 로드는 같지만,
  포인트 보유 여부 확인, 환불 불가, 다른 api 실행 이라는 점에서 
  차이점이 있다.
  */
  const onPointPayment = ()=>{
    const { IMP } = window;
    if (!IMP) {
      setError('결제 스크립트 로드 실패');
      return;
    }
    IMP.init(import.meta.env.VITE_IMP_KEY);

    const discounted = Number((computedPrice * 0.7).toFixed(2));
    const paymentRequest = {
      pg: 'html5_inicis',
      pay_method: 'card',
      merchant_uid: `order_${reservation.id}_${reservation.petBusinessId}_${reservation.userId}_${Date.now()}`,
      name: `reservation_${businessName}_point`,
      amount: discounted,
      reservation_id: reservation.id,
      buyer_email: user.email,
      buyer_name: user.name,
      buyer_tel: user.phone
    };

    IMP.request_pay( paymentRequest,
      async (response) =>{
      console.log('IMP response (point):', response);
        if(response.success){
          setLoading(true);
          try{
            const requestDTO = {
              impUid: response.imp_uid,
              merchantUid: response.merchant_uid,
              reservationId: reservation.id,
              amount: response.paid_amount || discounted,
              payMethod: response.pay_method,
              status: response.status,
              paidAt: response.paid_at
            };
            const payResponse = await ApiService.payments.pointregister(requestDTO);
            const data = payResponse.data;
            if(data.result){
              setResult(data.paymentResult);
            }else{
              setError('포인트 결제 검증에 실패했습니다.');
            }
          } catch (e) {
            console.error(e);
            setError('서버 통신 중 오류가 발생했습니다.');
          } finally {
            setLoading(false);
          }
        }else{
          setError(response.error_msg || '포인트 결제에 실패했습니다.');
        }
      }
    );
  };

  const onPayment = ()=>{

    const { IMP } = window;
    if (!IMP) {
      setError('결제 스크립트 로드 실패');
      return;
    }
    IMP.init(import.meta.env.VITE_IMP_KEY);

    const paymentRequest = {
      pg: 'html5_inicis',
      pay_method: 'card',
      merchant_uid: `order_${reservation.id}_${reservation.petBusinessId}_${reservation.userId}_${Date.now()}`,
      name: `reservation_${businessName}`,
      amount: computedPrice,
      reservation_id: reservation.id,
      buyer_email: user.email,
      buyer_name: user.name,
      buyer_tel: user.phone
    };
    IMP.request_pay( paymentRequest, 
      async (response) => {
        console.log('IMP response:', response);
        if(response.success){
          try{
            setLoading(true);
            const requestDTO = {
              impUid: response.imp_uid,
              merchantUid: response.merchant_uid,
              reservationId: reservation.id,
              amount: response.paid_amount || computedPrice,
              payMethod: response.pay_method,
              status: response.status,
              paidAt: response.paid_at
            };

            const payresponse= await ApiService.payments.register(requestDTO);
            const data=payresponse.data;

            if(data.result){
              setResult(data.paymentResult);
            }else{
              setError("결제 검증에 실패했습니다.");
            }
          }catch(e){
            console.error(e);
            setError('서버 통신 중 오류가 발생했습니다.');
          }finally{
            setLoading(false);
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

      <button
        onClick={onPayment}
        className="btn btn-primary w-100"
        disabled={loading || initialLoading || !computedPrice}
      >
        {loading ? '처리 중...' : '결제 요청하기'}
      </button>

      <button
        onClick={onPointPayment}
        className="btn btn-primary w-100"
        disabled={loading || initialLoading || !computedPrice}
      >
        {loading ? '처리 중 ...':'포인트를 이용한 결제'}
      </button>

      {result && (
        <div className="alert alert-success mt-3">
          <h5>결제 요청 성공</h5>
          <pre>{result}</pre>
        </div>
      )}
      {error && (
        <div className="alert alert-danger mt-3">{error}</div>
      )}
    </div>
  );
}

export default PaymentForm;
