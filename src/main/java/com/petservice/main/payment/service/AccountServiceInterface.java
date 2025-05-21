package com.petservice.main.payment.service;

import com.petservice.main.payment.database.dto.AccountDTO;
import org.springframework.data.domain.Page;


public interface AccountServiceInterface {

  public Page<AccountDTO> getAccountList(int page, int size);
  public AccountDTO getAccountByUserId(Long user_id);
  public AccountDTO getAccountByUserLoginId(String userLoginId);
  public AccountDTO getAccountByBusinessId(Long business_id);
  public AccountDTO getAccountByBusinessRegisterNumber(String register_number);
  public AccountDTO getMasterAccount();
  public AccountDTO registerAccount(AccountDTO accountDTO);
  public boolean deleteAccount(Long id);
  public AccountDTO updateAccount(Long id, int amount);

  public boolean validationAccount(AccountDTO accountDTO);
}
