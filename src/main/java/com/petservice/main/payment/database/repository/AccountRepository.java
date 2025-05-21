package com.petservice.main.payment.database.repository;

import com.petservice.main.payment.database.entity.Account;
import com.petservice.main.payment.database.entity.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

  Account findByUser_Id(Long id);

  Account findByUser_UserLoginId(String userLoginId);

  @Query("SELECT a FROM Account a WHERE a.accountType = :type")
  Account findByType(@Param("type") AccountType type);
}
