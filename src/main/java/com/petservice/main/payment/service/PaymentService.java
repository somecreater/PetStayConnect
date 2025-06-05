package com.petservice.main.payment.service;

import com.petservice.main.business.database.entity.Reservation;
import com.petservice.main.business.database.entity.ReservationStatus;
import com.petservice.main.business.database.repository.ReservationRepository;
import com.petservice.main.payment.database.dto.AccountDTO;
import com.petservice.main.payment.database.dto.PaymentCancelRequestDTO;
import com.petservice.main.payment.database.dto.PaymentDTO;
import com.petservice.main.payment.database.dto.PaymentRequestDTO;
import com.petservice.main.payment.database.entity.Payment;
import com.petservice.main.payment.database.entity.PaymentStatus;
import com.petservice.main.payment.database.entity.RefundStatus;
import com.petservice.main.payment.database.mapper.PaymentMapper;
import com.petservice.main.payment.database.repository.PaymentRepository;
import com.petservice.main.user.database.entity.User;
import com.petservice.main.user.database.repository.UserRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService implements PaymentServiceInterface{

  private final PaymentRepository paymentRepository;
  private final ReservationRepository reservationRepository;
  private final UserRepository userRepository;
  private final PaymentMapper paymentMapper;

  private final AccountServiceInterface accountService;
  private final IamportClient iamportClient;

  @Value("${payment.service.fee-rate}")
  private BigDecimal serviceFeeRate;

  @Override
  @Transactional(readOnly = true)
  public Page<PaymentDTO> PaymentListByBusiness(Long business_id, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("transactionTime").descending());
    Page<Payment> paymentsPage = paymentRepository.findByReservation_PetBusiness_Id(business_id, pageable);
    if(paymentsPage == null){
      return null;
    }
    return paymentsPage.map(paymentMapper::toDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PaymentDTO> PaymentListByUser(String userLoginId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("transactionTime").descending());
    Page<Payment> paymentsPage = paymentRepository.findByReservation_User_UserLoginId(userLoginId, pageable);
    if(paymentsPage == null){
      return null;
    }
    return paymentsPage.map(paymentMapper::toDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public PaymentDTO getPayment(Long payment_id){
    Payment payment = paymentRepository.findById(payment_id).orElse(null);

    if(payment == null){
      return null;
    }

    return paymentMapper.toDTO(payment);
  }

  //약 10%의 수수료를 때서 사업자에 전달
  @Override
  @Transactional
  public PaymentDTO RegisterPayment(PaymentRequestDTO dto) {

    Payment payment=new Payment();
    Payment saved;
    if(Objects.equals(dto.getStatus(), "failed")||!ValidationPayment(dto)){
      return null;
    }
    Reservation reservation=reservationRepository.findById(dto.getReservationId()).orElse(null);
    if(reservation == null){
      return null;
    }
    User user = reservation.getUser();
    AccountDTO consumer_account = accountService.getAccountByUserId(user.getId());
    AccountDTO business_account = accountService.getAccountByBusinessId(
        reservation.getPetBusiness().getId());
    AccountDTO manager_account = accountService.getMasterAccount();

    BigDecimal amount = dto.getAmount();
    BigDecimal expectedTotal =
        BigDecimal.valueOf(reservation.getPetBusiness().getMaxPrice())
            .multiply(BigDecimal.valueOf(reservation.getPetReservationList().size()));
    if(amount.compareTo(expectedTotal) != 0){
      return null;
    }
    BigDecimal fee = amount.multiply(serviceFeeRate)
        .setScale(2, RoundingMode.HALF_UP);

    payment.setReservation(reservation);
    payment.setImpUid(dto.getImpUid());
    payment.setMerchantUid(dto.getMerchantUid());
    payment.setAmount(amount);
    payment.setFeeRate(serviceFeeRate);
    payment.setServiceFee(fee);
    payment.setPaymentMethod(dto.getPayMethod());

    if(!Objects.equals(dto.getPayMethod(), "vbank")) {
      payment.setPaymentStatus(PaymentStatus.PAID);
      reservation.setStatus(ReservationStatus.CONFIRMED);
      user.setPoint(user.getPoint() + fee.intValue());

      accountService.updateAccount(consumer_account.getId(), amount.negate().intValue());
      accountService.updateAccount(business_account.getId(), amount.subtract(fee).intValue());
      accountService.updateAccount(manager_account.getId(), fee.intValue());

      reservationRepository.save(reservation);
      userRepository.save(user);
    }else{
      payment.setPaymentStatus(
        "ready".equalsIgnoreCase(dto.getStatus()) ? PaymentStatus.READY : PaymentStatus.FAILED
      );
    }
    payment.setTransactionId(dto.getImpUid());
    payment.setTransactionTime(
      LocalDateTime.ofInstant(Instant.ofEpochSecond(dto.getPaidAt()), ZoneId.systemDefault()));
    payment.setRefundStatus(RefundStatus.NONE);
    payment.setRefundAmount(BigDecimal.ZERO);

    saved = paymentRepository.save(payment);
    return paymentMapper.toDTO(saved);
  }

  @Override
  @Transactional
  public PaymentDTO RegisterVBankPayment(String impUid, String status){
    Payment payment= paymentRepository.findByImpUid(impUid).orElse(null);
    if(payment == null){
      return null;
    }
    Payment saved;
    User user=payment.getReservation().getUser();
    AccountDTO consumer_account = accountService.getAccountByUserId(user.getId());
    AccountDTO business_account = accountService.getAccountByBusinessId(
        payment.getReservation().getPetBusiness().getId());
    AccountDTO master_account = accountService.getMasterAccount();
    Reservation reservation= payment.getReservation();
    if("paid".equalsIgnoreCase(status)) {
      if(payment.getPaymentStatus() == PaymentStatus.POINT_PAID_READY){
        payment.setPaymentStatus(PaymentStatus.POINT_PAID);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);

        BigDecimal amount = payment.getAmount();
        BigDecimal orgAmount = payment.getAmount()
            .multiply(BigDecimal.valueOf(10))
            .divide(BigDecimal.valueOf(7), 0, RoundingMode.HALF_UP);
        BigDecimal fee = payment.getServiceFee();
        accountService.updateAccount(consumer_account.getId(), amount.negate().intValue());
        accountService.updateAccount(business_account.getId(), orgAmount.subtract(fee).intValue());

        BigDecimal subRate = new BigDecimal("0.30");
        BigDecimal subAmount =orgAmount.multiply(subRate).setScale(0, RoundingMode.DOWN);
        accountService.updateAccount(master_account.getId(), fee.intValue());
        accountService.updateAccount(master_account.getId(), subAmount.negate().intValue());
        user.setPoint(user.getPoint()-subAmount.intValue());
        userRepository.save(user);
      }else {
        payment.setPaymentStatus(PaymentStatus.PAID);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);

        BigDecimal amount = payment.getAmount();
        BigDecimal fee = payment.getServiceFee();
        accountService.updateAccount(consumer_account.getId(), amount.negate().intValue());
        accountService.updateAccount(business_account.getId(), amount.subtract(fee).intValue());
        accountService.updateAccount(master_account.getId(), fee.intValue());

        user.setPoint(user.getPoint() + fee.intValue());
        userRepository.save(user);
      }
    }else if ("failed".equalsIgnoreCase(status)
        || "cancelled".equalsIgnoreCase(status)) {
      // 입금 실패 혹은 기한 만료
      payment.setPaymentStatus(PaymentStatus.FAILED);
    }
    saved=paymentRepository.save(payment);
    return paymentMapper.toDTO(saved);
  }

  /*
  포인트 할인을 이용한 결제, 환불 불가, 포인트 적립 안됨,
  30 % 할인, 사업자에게는 수수료 빼고, 정산 금액 지급,
  할인된 값은 관리자 계좌에서 차감
  */
  @Override
  @Transactional
  public PaymentDTO RegisterPaymentByPoint(PaymentRequestDTO dto){

    Payment payment=new Payment();
    Payment saved;
    if(Objects.equals(dto.getStatus(), "failed")||!ValidationPayment(dto)){
      return null;
    }
    Reservation reservation=reservationRepository
        .findById(dto.getReservationId()).orElse(null);
    if(reservation == null){
      return null;
    }
    User user= reservation.getUser();
    AccountDTO consumer_account = accountService.getAccountByUserId(user.getId());
    AccountDTO business_account = accountService
        .getAccountByBusinessId(reservation.getPetBusiness().getId());
    AccountDTO manager_account = accountService.getMasterAccount();

    BigDecimal amount = dto.getAmount();
    BigDecimal expectedOrg = BigDecimal.valueOf(reservation.getPetBusiness().getMaxPrice())
        .multiply(BigDecimal.valueOf(reservation.getPetReservationList().size()));
    BigDecimal discountRate= new BigDecimal("0.7");
    BigDecimal discountPrice= new BigDecimal("0.3");
    BigDecimal expectedDiscounted = expectedOrg.multiply(discountRate)
        .setScale(0, RoundingMode.DOWN);
    BigDecimal expectedDiscountPrice= expectedOrg.multiply(discountPrice)
        .setScale(0, RoundingMode.DOWN);
    if(amount.compareTo(expectedDiscounted) != 0){
      return null;
    }
    if(user.getPoint() < expectedDiscountPrice.intValue()){
      throw new IllegalStateException("포인트 부족");
    }

    BigDecimal fee = expectedOrg.multiply(serviceFeeRate).setScale(2, RoundingMode.HALF_UP);

    payment.setReservation(reservation);
    payment.setImpUid(dto.getImpUid());
    payment.setMerchantUid(dto.getMerchantUid());
    payment.setAmount(amount);
    payment.setFeeRate(serviceFeeRate);
    payment.setServiceFee(fee);
    payment.setPaymentMethod(dto.getPayMethod());

    if(!Objects.equals(dto.getPayMethod(), "vbank")) {
      payment.setPaymentStatus(PaymentStatus.POINT_PAID);
      reservation.setStatus(ReservationStatus.CONFIRMED);

      accountService.updateAccount(consumer_account.getId(), amount.negate().intValue());
      accountService.updateAccount(business_account.getId(), expectedOrg.subtract(fee).intValue());

      //할인된 가격은 관리자 계좌에서 인출
      BigDecimal subPrice = expectedOrg.multiply(BigDecimal.valueOf(0.3));
      accountService.updateAccount(manager_account.getId(), fee.intValue());
      accountService.updateAccount(manager_account.getId(), subPrice.negate().intValue());
      user.setPoint(user.getPoint()-expectedDiscountPrice.intValue());
      reservationRepository.save(reservation);
    }else{
      payment.setPaymentStatus(
          "ready".equalsIgnoreCase(dto.getStatus()) ? PaymentStatus.POINT_PAID_READY : PaymentStatus.FAILED
      );
    }
    payment.setTransactionId(dto.getImpUid());
    payment.setTransactionTime(
        LocalDateTime.ofInstant(Instant.ofEpochSecond(dto.getPaidAt()), ZoneId.systemDefault()));
    payment.setRefundStatus(RefundStatus.NONE);
    payment.setRefundAmount(BigDecimal.ZERO);

    saved = paymentRepository.save(payment);
    return paymentMapper.toDTO(saved);
  }

  @Override
  @Transactional
  public PaymentDTO CancelPayment(PaymentCancelRequestDTO dto) {
    String impUid=dto.getImpUid();
    String merchantUid=dto.getMerchantUid();
    Payment exPayment= paymentRepository.findByImpUidAndMerchantUid(impUid,merchantUid).orElse(null);
    if(exPayment == null){
      return null;
    }
    if(exPayment.getPaymentStatus() == PaymentStatus.FAILED
      || exPayment.getPaymentStatus() == PaymentStatus.CANCELED
      || exPayment.getPaymentStatus() == PaymentStatus.READY
      || exPayment.getPaymentStatus() == PaymentStatus.POINT_PAID
      || exPayment.getRefundStatus() != RefundStatus.NONE){
      return null;
    }

    Reservation reservation=reservationRepository.findById(exPayment.getReservation().getId())
        .orElse(null);
    if(reservation == null){
      throw new RuntimeException("예약이 존재하지 않습니다.");
    }

    User user= reservation.getUser();
    AccountDTO consumerAccount= accountService.getAccountByUserId(user.getId());
    AccountDTO businessAccount= accountService.getAccountByBusinessId(
        reservation.getPetBusiness().getId());
    AccountDTO masterAccount= accountService.getMasterAccount();

    BigDecimal fee = exPayment.getServiceFee();
    if(user.getPoint() < fee.intValue()){
      exPayment.setRefundStatus(RefundStatus.REJECTED);
      paymentRepository.save(exPayment);
      throw new IllegalStateException("포인트 부족");
    }

    CancelData cancelData =
        new CancelData(exPayment.getImpUid(), true, exPayment.getAmount());
    cancelData.setReason(dto.getReason());
    IamportResponse<com.siot.IamportRestClient.response.Payment> iamportResponse;
    try{
      iamportResponse = iamportClient.cancelPaymentByImpUid(cancelData);
    } catch (IamportResponseException | IOException e) {
      log.error("Iamport 환불 실패", e);
      exPayment.setRefundStatus(RefundStatus.REJECTED);
      paymentRepository.save(exPayment);
      throw new RuntimeException("Iamport 환불 실패(문의 게시판이나 아래의 번호로 문의 주세요!): "
          + e.getMessage(), e);
    }

    com.siot.IamportRestClient.response.Payment cancelled = iamportResponse.getResponse();

    BigDecimal cancelledAmt = BigDecimal.valueOf(
        cancelled.getCancelAmount().longValue());
    exPayment.setRefundStatus(RefundStatus.COMPLETED);
    exPayment.setRefundAmount(cancelledAmt);
    exPayment.setPaymentStatus(PaymentStatus.CANCELED);

    accountService.updateAccount(consumerAccount.getId(), cancelledAmt.intValue());
    accountService.updateAccount(businessAccount.getId(), cancelledAmt.subtract(fee).negate().intValue());
    accountService.updateAccount(masterAccount.getId(), fee.negate().intValue());
    user.setPoint(user.getPoint() - fee.intValue());

    paymentRepository.save(exPayment);
    userRepository.save(user);

    return paymentMapper.toDTO(exPayment);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean ValidationPayment(PaymentRequestDTO dto){

    if(isBlank(dto.getImpUid())
    || isBlank(dto.getMerchantUid())
    || dto.getReservationId() == null
    || dto.getAmount() == null
    || isBlank(dto.getPayMethod())
    || isBlank(dto.getStatus())){
      return false;
    }

    if(paymentRepository.existsByReservation_IdOrImpUidOrMerchantUid(
        dto.getReservationId(), dto.getImpUid(), dto.getMerchantUid())){
      return false;
    }

    return true;
  }


  @Override
  public boolean isBlank(String str){
    return str == null || str.trim().isEmpty();
  }
}
