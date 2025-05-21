package com.petservice.main.payment.service;

import com.petservice.main.business.database.entity.PetBusiness;
import com.petservice.main.business.database.repository.PetBusinessRepository;
import com.petservice.main.payment.database.dto.AccountDTO;
import com.petservice.main.payment.database.entity.Account;
import com.petservice.main.payment.database.entity.AccountType;
import com.petservice.main.payment.database.mapper.AccountMapper;
import com.petservice.main.payment.database.repository.AccountRepository;
import com.petservice.main.user.database.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService implements AccountServiceInterface{

  private final AccountRepository accountRepository;
  private final PetBusinessRepository petBusinessRepository;

  private final AccountMapper accountMapper;

  @Override
  @Transactional(readOnly = true)
  public Page<AccountDTO> getAccountList(int page, int size) {
    Pageable pageable= PageRequest.of(page,size, Sort.by("id").descending());
    Page<Account> accounts=accountRepository.findAll(pageable);
    return accounts.map(accountMapper::toDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public AccountDTO getAccountByUserId(Long user_id) {
    Account account = accountRepository.findByUser_Id(user_id);
    return accountMapper.toDTO(account);
  }

  @Override
  @Transactional(readOnly = true)
  public AccountDTO getAccountByUserLoginId(String userLoginId) {
    Account account = accountRepository.findByUser_UserLoginId(userLoginId);
    return accountMapper.toDTO(account);
  }

  @Override
  @Transactional(readOnly = true)
  public AccountDTO getAccountByBusinessId(Long business_id) {
    PetBusiness petBusiness=petBusinessRepository.findById(business_id).orElse(null);
    if(petBusiness ==null){
      return null;
    }
    User user=petBusiness.getUser();
    Account account=accountRepository.findByUser_Id(user.getId());
    return accountMapper.toDTO(account);
  }

  @Override
  @Transactional(readOnly = true)
  public AccountDTO getAccountByBusinessRegisterNumber(String register_number) {
    PetBusiness petBusiness=petBusinessRepository.findByRegistrationNumber(
        register_number
    );
    if(petBusiness ==null){
      return null;
    }
    User user=petBusiness.getUser();
    Account account=accountRepository.findByUser_Id(user.getId());
    return accountMapper.toDTO(account);
  }

  @Override
  @Transactional(readOnly = true)
  public AccountDTO getMasterAccount(){
    Account account=accountRepository.findByType(AccountType.SERVER);
    return accountMapper.toDTO(account);
  }

  @Override
  @Transactional
  public AccountDTO registerAccount(AccountDTO accountDTO) {
    if(accountDTO.getId()!=null){
      return null;
    }
    if(validationAccount(accountDTO)){
      return null;
    }
    Account account= accountMapper.toEntity(accountDTO);
    Account saved=accountRepository.save(account);
    return accountMapper.toDTO(saved);
  }

  @Override
  @Transactional
  public boolean deleteAccount(Long id) {
    Account delete=accountRepository.findById(id).orElse(null);
    if(delete == null){
      return false;
    }
    accountRepository.deleteById(id);
    return true;
  }

  @Override
  @Transactional
  public AccountDTO updateAccount(Long id, int amount) {
    Account account =accountRepository.findById(id).orElse(null);
    if (account == null) return null;

    BigDecimal newAmount = account.getAmount().add(BigDecimal.valueOf(amount));
    if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
      return null;
    }
    account.setAmount(newAmount);
    Account update=accountRepository.save(account);
    return accountMapper.toDTO(update);
  }

  @Override
  public boolean validationAccount(AccountDTO accountDTO){
    return accountDTO != null
        && accountDTO.getAccountType() != null
        && accountDTO.getAmount() != null
        && accountDTO.getUserId() != null;
  }
}
