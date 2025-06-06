package com.petservice.main.payment.service;


import com.petservice.main.payment.database.dto.PaymentCancelRequestDTO;
import com.petservice.main.payment.database.dto.PaymentDTO;
import com.petservice.main.payment.database.dto.PaymentRequestDTO;
import org.springframework.data.domain.Page;

public interface PaymentServiceInterface {

  public Page<PaymentDTO> PaymentListByBusiness(Long business_id, int page, int size);
  public Page<PaymentDTO> PaymentListByUser(String userLoginId, int page, int size);
  public PaymentDTO getPayment(Long payment_id);
  public PaymentDTO RegisterPayment(PaymentRequestDTO dto);
  public PaymentDTO RegisterVBankPayment(String impUid, String status);
  public PaymentDTO RegisterPaymentByPoint(PaymentRequestDTO dto);
  public PaymentDTO CancelPayment(PaymentCancelRequestDTO dto);

  public boolean ValidationPayment(PaymentRequestDTO dto);
  public boolean isBlank(String str);
}
