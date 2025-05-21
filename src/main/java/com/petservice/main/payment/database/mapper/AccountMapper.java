package com.petservice.main.payment.database.mapper;

import com.petservice.main.payment.database.dto.AccountDTO;
import com.petservice.main.payment.database.entity.Account;
import com.petservice.main.user.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountMapper {

  private final UserRepository userRepository;

  public Account toEntity(AccountDTO dto){
    Account account = new Account();
    account.setId(dto.getId());
    account.setAccountType(dto.getAccountType());
    account.setAmount(dto.getAmount());
    if(dto.getUserId()!=null){
      account.setUser(userRepository.findById(dto.getUserId()).orElse(null));
    }

    return account;
  }

  public AccountDTO toDTO(Account entity){
    AccountDTO accountDTO = new AccountDTO();
    accountDTO.setId(entity.getId());
    accountDTO.setAccountType(entity.getAccountType());
    accountDTO.setAmount(entity.getAmount());
    if(entity.getUser() != null){
      accountDTO.setUserId(entity.getUser().getId());
      accountDTO.setUserLoginId(entity.getUser().getUserLoginId());
    }

    return accountDTO;
  }
}
